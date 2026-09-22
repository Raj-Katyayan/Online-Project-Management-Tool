CREATE DATABASE IF NOT EXISTS project_management;
USE project_management;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','MANAGER','MEMBER') NOT NULL DEFAULT 'MEMBER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    priority ENUM('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
    status ENUM('PLANNED','IN_PROGRESS','COMPLETED','ON_HOLD') DEFAULT 'PLANNED',
    manager_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_projects_manager FOREIGN KEY (manager_id)
        REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS project_members (
    project_id INT NOT NULL,
    user_id INT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_project_members_project FOREIGN KEY (project_id)
        REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_project_members_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority ENUM('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
    status ENUM('TODO','IN_PROGRESS','COMPLETED','BLOCKED') DEFAULT 'TODO',
    start_date DATE,
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id)
        REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS task_assignments (
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (task_id, user_id),
    CONSTRAINT fk_task_assignments_task FOREIGN KEY (task_id)
        REFERENCES tasks(task_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_task_assignments_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS activity_logs (
    activity_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    project_id INT,
    action VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activity_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_activity_project FOREIGN KEY (project_id)
        REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE
);
