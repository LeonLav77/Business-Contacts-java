CREATE TABLE IF NOT EXISTS contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    department VARCHAR(255),
    company_id BIGINT,
    created_at DATE,
    importance VARCHAR(10),
    custom_note TEXT,
    FOREIGN KEY (company_id) REFERENCES companies(id)
);
