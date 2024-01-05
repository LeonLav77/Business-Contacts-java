package production.bussines_contacts.models;

import production.bussines_contacts.interfaces.Editable;
import production.bussines_contacts.models.User;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;

public class ChangeDataModel<T extends Editable<T>> implements Serializable {
    private final User user;
    private final Date changeTime;
    private final Map<String, Map<String, String>> differences;
    private final T changedObject;

    private ChangeDataModel(Builder<T> builder) {
        this.user = builder.user;
        this.changeTime = builder.changeTime;
        this.differences = builder.differences;
        this.changedObject = builder.changedObject;
    }

    public static class Builder<T extends Editable<T>> implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private User user;
        private Date changeTime;
        private Map<String, Map<String, String>> differences;
        private T changedObject;

        public Builder<T> withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder<T> atTime(Date changeTime) {
            this.changeTime = changeTime;
            return this;
        }

        public Builder<T> withDifferences(Map<String, Map<String, String>> differences) {
            this.differences = differences;
            return this;
        }

        public Builder<T> forObject(T changedObject) {
            this.changedObject = changedObject;
            return this;
        }

        public ChangeDataModel<T> build() {
            return new ChangeDataModel<>(this);
        }
    }
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String changeTimeFormatted = dateFormat.format(changeTime);

        String objectType = changedObject.getClass().getSimpleName();
        String objectIdentifier = String.valueOf(changedObject.getId());

        StringJoiner changesJoiner = new StringJoiner(", ");
        for (Map.Entry<String, Map<String, String>> entry : differences.entrySet()) {
            String field = entry.getKey();
            Map<String, String> change = entry.getValue();
            String oldValue = change.get("old");
            String newValue = change.get("new");
            changesJoiner.add(field + ": \"" + oldValue + "\" to \"" + newValue + "\"");
        }

        return user.getName() + " changed " + objectType + " " + objectIdentifier +
                " on " + changeTimeFormatted + ": " + changesJoiner;
    }
}
