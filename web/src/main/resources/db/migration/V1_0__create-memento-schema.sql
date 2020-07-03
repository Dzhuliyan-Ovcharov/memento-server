CREATE DATABASE IF NOT EXISTS memento;
USE memento;

CREATE TABLE IF NOT EXISTS ad_types (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(255),

    UNIQUE INDEX uidx_ad_type (`type`)
);

CREATE TABLE IF NOT EXISTS cities (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),

    UNIQUE INDEX uidx_city_name (`name`)
);

CREATE TABLE IF NOT EXISTS neighborhoods (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    city_id int,

    UNIQUE INDEX uidx_neighborhood_name (`name`),
    CONSTRAINT fk_neighborhoods_city_id FOREIGN KEY (city_id)
    REFERENCES cities(id)
);

CREATE TABLE IF NOT EXISTS estate_features (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    feature VARCHAR(255),

    UNIQUE INDEX uidx_estate_feature (feature)
);

CREATE TABLE IF NOT EXISTS estate_types (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(30),

    UNIQUE INDEX uidx_estate_type (`type`)
);

CREATE TABLE IF NOT EXISTS floors (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `number` int,

    UNIQUE INDEX uidx_floor_number (`number`)
);

CREATE TABLE IF NOT EXISTS roles (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    permission varchar(255),

    UNIQUE INDEX uidx_role (permission)
);

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    agency_name varchar(255),
    agency_phone_number varchar(255),
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    phone_number varchar(255),
    role_id int,

    UNIQUE INDEX uidx_user_email (email),
    UNIQUE INDEX uidx_user_phone_number (phone_number),
    CONSTRAINT fk_users_roles_id FOREIGN KEY (role_id)
    REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS estates (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description varchar(255),
    price varchar(255),
    built_up_area decimal(5,2),
    pure_area decimal(5,2),
    ad_type_id int,
    estate_type_id int,
    floor_id int,
    user_id int,

    CONSTRAINT fk_estates_ad_type_id FOREIGN KEY (ad_type_id)
    REFERENCES ad_types(id),

    CONSTRAINT fk_estates_estate_type_id FOREIGN KEY (estate_type_id)
    REFERENCES estate_types(id),

    CONSTRAINT fk_estates_floor_id FOREIGN KEY (floor_id)
    REFERENCES floors(id),

    CONSTRAINT fk_estates_user_id FOREIGN KEY (user_id)
    REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS estates_estate_features (
    estate_id INT,
    estate_feature_id INT,

    PRIMARY KEY (estate_id, estate_feature_id),

    CONSTRAINT fk_estates_estate_features_estate_id FOREIGN KEY (estate_id)
    REFERENCES estates(id),

    CONSTRAINT fk_estates_estate_features_estate_feature_id FOREIGN KEY (estate_feature_id)
    REFERENCES estate_features(id)
);

CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    expiry_time timestamp,
    is_email_verified bit(1),
    token varchar(255),
    user_id int,

    UNIQUE INDEX uidx_token (token),

    CONSTRAINT fk_email_verification_tokens_user_id FOREIGN KEY (user_id)
    REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS images (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(255),
    estate_id int,

    UNIQUE INDEX uidx_image_name (`name`),

    CONSTRAINT fk_images_estate_id FOREIGN KEY (estate_id)
    REFERENCES estates(id)
);
