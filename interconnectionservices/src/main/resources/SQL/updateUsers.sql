ALTER TABLE users ADD accessToken VARCHAR(250);
ALTER TABLE users ADD socialType VARCHAR(60);
ALTER TABLE users MODIFY COLUMN emailId varchar(50);
ALTER TABLE users MODIFY COLUMN password varchar(50);
ALTER TABLE users MODIFY COLUMN mobileNo varchar(50);
ALTER TABLE users ADD state VARCHAR(60);
ALTER TABLE users ADD isConnected varchar(10);
ALTER TABLE users ADD attemptNumber int NOT NULL DEFAULT 0;
ALTER TABLE api ADD maxLoginHit int NOT NULL DEFAULT 5;
ALTER TABLE `users` ADD `createDate` DATE NOT NULL AFTER `attemptNumber`;
ALTER TABLE `api` CHANGE `activationLink` `activationLink` VARCHAR( 512 ) CHARACTER SET cp866 COLLATE cp866_general_ci NOT NULL DEFAULT'http://112.196.7.220/php/index.php/verficationscreen?user_id={userId}&phone_code={phone_code}&email_code={email_code}';