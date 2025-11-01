-- This file will be executed on startup to seed the database with initial data.

-- Insert a customer (ID will be 1)
INSERT INTO users (username, password, email, role) VALUES ('customer1', 'password123', 'customer@example.com', 'CUSTOMER');
-- Insert an agent (ID will be 2)
INSERT INTO users (username, password, email, role) VALUES ('agent_smith', 'password123', 'agent@example.com', 'AGENT');

-- Insert a sample ticket created by the customer (user ID 1)
INSERT INTO tickets (title, description, status, priority, created_by_user_id, created_at)
VALUES ('Cannot Print Documents', 'The printer on the second floor is not responding.', 'OPEN', 'MEDIUM', 1, CURRENT_TIMESTAMP);