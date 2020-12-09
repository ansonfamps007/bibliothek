-- -----------------------------------------------------
-- Schema bibliothek
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bibliothek` DEFAULT CHARACTER SET utf8 ;
USE `bibliothek` ;

-- -----------------------------------------------------
-- Table `bibliothek`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`category` (
  `id` INT AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `bibliothek`.`language`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`languages` (
  `id` INT AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `bibliothek`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`users` (
  `id` INT AUTO_INCREMENT ,
  `username` VARCHAR(255) NOT NULL COMMENT 'Email id as username',
  `name` VARCHAR(255),
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(16) NULL,
  `is_active` TINYINT(1) NULL COMMENT 'User will active after email verification',
  `confirm_token` TINYTEXT NULL,
  `reset_token` TINYTEXT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `fcm_id` TINYTEXT NULL,
  `last_updated_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) );


-- -----------------------------------------------------
-- Table `bibliothek`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`author` (
  `id` INT AUTO_INCREMENT ,
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `bibliothek`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`book` (
  `id` INT AUTO_INCREMENT,
  `status` VARCHAR(45) NULL,
  `qr_code` VARCHAR(45) NULL,
  `is_approved` TINYINT(1) NULL DEFAULT 1,
  `is_deleted` TINYINT(1) NULL DEFAULT 0,
  `cover_url` TINYTEXT NULL,
  `cover_thumb_url` TINYTEXT NULL,
  `title` VARCHAR(255) NULL,
  `book_desc` text NOT NULL,
  `author_id` INT NOT NULL,
  `language_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `donated_id` INT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `language_id_idx` (`language_id` ASC) ,
  INDEX `category_id_idx` (`category_id` ASC) ,
  INDEX `donated_by_idx` (`donated_id` ASC) ,
  INDEX `book_author_id_idx` (`author_id` ASC) ,
  CONSTRAINT `book_language_id`
    FOREIGN KEY (`language_id`)
    REFERENCES `bibliothek`.`languages` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `bibliothek`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_donated_by`
    FOREIGN KEY (`donated_id`)
    REFERENCES `bibliothek`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_author_id`
    FOREIGN KEY (`author_id`)
    REFERENCES `bibliothek`.`author` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `bibliothek`.`book_issue_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`book_issue_details` (
  `id` INT AUTO_INCREMENT ,
  `status` VARCHAR(45) NOT NULL,
  `book_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `issued_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `return_dt` TIMESTAMP NULL,
  `returned_on` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `book_id_idx` (`book_id` ASC) ,
  INDEX `user_id_idx` (`user_id` ASC) ,
  CONSTRAINT `book_issue_book_id`
    FOREIGN KEY (`book_id`)
    REFERENCES `bibliothek`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_issue_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `bibliothek`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `bibliothek`.`watch`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`watch` (
  `id` INT AUTO_INCREMENT ,
  `book_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  INDEX `book_id_idx` (`book_id` ASC) ,
  INDEX `user_id_idx` (`user_id` ASC) ,
  PRIMARY KEY (`id`),
  CONSTRAINT `watch_book_id`
    FOREIGN KEY (`book_id`)
    REFERENCES `bibliothek`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `watch_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `bibliothek`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
	UNIQUE KEY `book_user_id` (`book_id`,`user_id`)
	);


-- -----------------------------------------------------
-- Table `bibliothek`.`preferences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bibliothek`.`preferences` (
  `id` INT AUTO_INCREMENT ,
  `is_down` TINYINT(1) NULL,
  `down_reason` TEXT NULL,
  `latest_version_code` INT NULL,
  `latest_version_message` TEXT NULL,
  PRIMARY KEY (`id`));
  

ALTER TABLE book AUTO_INCREMENT = 100;

alter table book add column expiry_date timestamp null default null;

INSERT INTO `preferences` VALUES (1,0,'Technical Reason',1011,'Down');
