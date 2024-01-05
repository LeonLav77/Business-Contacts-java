package production.bussines_contacts.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;
import production.bussines_contacts.APIHandler.ChatGPTHandler;
import production.bussines_contacts.Application;
import production.bussines_contacts.exceptions.ChatGPTAnswerException;
import production.bussines_contacts.exceptions.ChatGPTRequestException;
import production.bussines_contacts.exceptions.FileNotWorkingException;
import production.bussines_contacts.exceptions.InvalidCSVException;
import production.bussines_contacts.interfaces.Importable;
import production.bussines_contacts.interfaces.Loggable;
import production.bussines_contacts.models.*;
import production.bussines_contacts.records.ChatGPTResponse;
import production.bussines_contacts.utils.FunctionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
public final class ImportOptionsController implements Loggable {
    private static final Logger logger = Logger.getLogger(ImportOptionsController.class.getName());
    @FXML
    private RadioButton precheckAI;
    @FXML
    private RadioButton trustAI;
    private static final String PROMPT_TEXT = "I have a CSV with the headers: \\n";
    private static final String BELOW_DATA_TEXT = ". Below is the content of the CSV: \"";
    private static final String ANSWER_TYPE_TEXT = ".\nPlease return TRUE or FALSE, in a JSON field 'valid'. \\n";
    private static final String CHECK_WITHOUT_FIX_TEXT = "If valid is FALSE, indicate the error in JSON field 'reason'. \\n";
    public void handleImportCompanies() {
        handleImport(new Company(), this::createCompanyFromCSVLine);
    }

    public void handleImportUsers() {
        handleImport(new Viewer(), this::createUserFromCSVLine);
    }

    public void handleImportContacts() {
        handleImport(new Contact(), this::createContactFromCSVLine);
    }

    public <T> void handleImport(Importable<T> importable, Function<String, T> createItemFunction) {
        try {
            File file = handleOpeningCSV();
            if (precheckAI.isSelected() && !validateCSVWithAI(file, importable)) {
                return;
            }

            List<T> items = importCSV(file, createItemFunction);
            importable.redirectToConfirmScreen(items);
        } catch (InvalidCSVException | IOException | FileNotWorkingException e) {
            FunctionUtils.handleException("CSV Processing Error", e, Alert.AlertType.WARNING, new ImportOptionsController());
        } catch (ChatGPTRequestException e) {
            FunctionUtils.handleException("AI request error, try importing without AI precheck", e, Alert.AlertType.ERROR, new ImportOptionsController());
        } catch (ChatGPTAnswerException e) {
            FunctionUtils.handleException("AI returned bad results", e, Alert.AlertType.ERROR, new ImportOptionsController());
        } catch (InterruptedException e) {
            FunctionUtils.handleException("InterruptedException", e, Alert.AlertType.ERROR, new ImportOptionsController());
        } catch (Exception e) {
            FunctionUtils.handleException("Unexpected Error", e, Alert.AlertType.ERROR, new ImportOptionsController());
        }
    }



    private boolean validateCSVWithAI(File file, Importable<?> importable) throws IOException, InterruptedException, ChatGPTRequestException, ChatGPTAnswerException {
        String CSVHeader = importable.getCSVHeader();
        ChatGPTResponse CSVValidByAI = callAI(file, CSVHeader);

        if (trustAI.isSelected() && !CSVValidByAI.content().optString("valid").equalsIgnoreCase("TRUE")) {
            FunctionUtils.showAlert(Alert.AlertType.WARNING, "Invalid CSV File", "The CSV file is invalid: " + CSVValidByAI.content().optString("reason"));
            return false;
        }
        return true;
    }

    private ChatGPTResponse callAI(File file, String CSVHeader) throws IOException, InterruptedException, ChatGPTRequestException {
        String fileContent = readFileAsString(file);
        String requestBody = buildJsonPayload(fileContent, CSVHeader);
        ChatGPTHandler chatGPTHandler = new ChatGPTHandler("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");
        return chatGPTHandler.simpleRequest(requestBody);
    }

    private String buildJsonPayload(String fileContent, String CSVHeader) {
        JSONArray messages = new JSONArray();
        String prompt = PROMPT_TEXT + CSVHeader + BELOW_DATA_TEXT + fileContent + ANSWER_TYPE_TEXT + CHECK_WITHOUT_FIX_TEXT;

        System.out.println(prompt);
        messages.put(new JSONObject()
                .put("role", "system")
                .put("content", "You are a program that checks CSV file formats. Verify if the headers match with each line of data."));

        messages.put(new JSONObject()
                .put("role", "user")
                .put("content", prompt));

        JSONObject response_format = new JSONObject()
                .put("type", "json_object");

        JSONObject requestBody = new JSONObject()
                .put("model", "gpt-3.5-turbo-1106")
                .put("seed", 14062003)
                .put("temperature", 0.2)
                .put("response_format", response_format)
                .put("messages", messages);

        return requestBody.toString();
    }


    private String readFileAsString(File file) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        }
        return fileContent.toString(); // Return the actual file content
    }

    private File handleOpeningCSV() throws FileNotWorkingException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(Application.getMainStage());
        if (file == null || !file.canRead()) {
            throw new FileNotWorkingException("Unable to read the selected file");
        }
        return file;
    }


    private <T> List<T> importCSV(File file, Function<String, T> createItemFunction) throws InvalidCSVException {
        List<T> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Read the first line (header) of the CSV file
            if (line == null) {
                throw new InvalidCSVException("Empty CSV file");
            }
            while ((line = br.readLine()) != null) {
                T item = createItemFunction.apply(line);
                if (item != null) {
                    items.add(item);
                } else {
                    throw new InvalidCSVException("Invalid CSV line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new InvalidCSVException("CSV file not found", e);
        } catch (IOException e) {
            throw new InvalidCSVException("Error reading CSV file", e);
        }
        return items;
    }

    private Company createCompanyFromCSVLine(String line) {
        return createObjectFromCSVLine(line, new Company());
    }

    private User createUserFromCSVLine(String line) {
        return createObjectFromCSVLine(line, new Viewer());
    }

    private Contact createContactFromCSVLine(String line) {
        return createObjectFromCSVLine(line, new Contact());
    }

    private <T> T createObjectFromCSVLine(String line, Importable<T> importable) {
        String[] values = line.split(",");
        if (values.length != importable.getNumberOfColumns()) {
            // Handle invalid CSV line, for example, by logging an error or returning null.
            return null;
        }
        return importable.createItem(values);
    }


}
