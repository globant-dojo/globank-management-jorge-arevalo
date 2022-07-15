-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: C:/bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml
-- Ran at: 7/15/22, 1:00 AM
-- Against: postgres@jdbc:postgresql://localhost:63519/postgresql?loggerLevel=OFF
-- Liquibase version: 4.9.1
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE public.databasechangeloglock (ID INTEGER NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM public.databasechangeloglock;

INSERT INTO public.databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE public.databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'CO-IT022064 (192.168.5.107)', LOCKGRANTED = NOW() WHERE ID = 1 AND LOCKED = FALSE;

-- Create Database Change Log Table
CREATE TABLE public.databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITHOUT TIME ZONE NOT NULL, ORDEREXECUTED INTEGER NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255), DEPLOYMENT_ID VARCHAR(10));

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-1::jorge.arevalo (generated)
CREATE TABLE public.account (id UUID NOT NULL, account_number VARCHAR(20) NOT NULL, account_type VARCHAR(20) NOT NULL, initial_balance DOUBLE PRECISION DEFAULT 0 NOT NULL, status BOOLEAN DEFAULT TRUE NOT NULL, client_id UUID NOT NULL, CONSTRAINT "accountPK" PRIMARY KEY (id));

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-1', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 1, '8:9da3fd8ce3616253d6f1fc2fe38fb1d0', 'createTable tableName=account', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-2::jorge.arevalo (generated)
CREATE TABLE public.client (password VARCHAR(50) NOT NULL, status BOOLEAN DEFAULT TRUE NOT NULL, id UUID NOT NULL, CONSTRAINT "clientPK" PRIMARY KEY (id));

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-2', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 2, '8:86c9ae0b3b7e50b8a7e31ed5a89e1937', 'createTable tableName=client', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-3::jorge.arevalo (generated)
CREATE TABLE public.movement (id UUID NOT NULL, balance DOUBLE PRECISION DEFAULT 0 NOT NULL, movement_date date NOT NULL, movement_type VARCHAR(20) NOT NULL, value DOUBLE PRECISION DEFAULT 0 NOT NULL, account_id UUID NOT NULL, CONSTRAINT "movementPK" PRIMARY KEY (id));

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-3', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 3, '8:11d6e472f5e420db75a245ad67041b27', 'createTable tableName=movement', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-4::jorge.arevalo (generated)
CREATE TABLE public.person (id UUID NOT NULL, address VARCHAR(100) NOT NULL, age INTEGER, gender VARCHAR(10) NOT NULL, identification VARCHAR(20) NOT NULL, name VARCHAR(100) NOT NULL, phone_number VARCHAR(20) NOT NULL, CONSTRAINT "personPK" PRIMARY KEY (id));

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-4', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 4, '8:cebf7860b9d28222360512ba73a98cf1', 'createTable tableName=person', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-5::jorge.arevalo (generated)
ALTER TABLE public.account ADD CONSTRAINT "FKkm8yb63h4ownvnlrbwnadntyn" FOREIGN KEY (client_id) REFERENCES public.client (id);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-5', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 5, '8:1fa17424e889c1fe18e776effd14a7d8', 'addForeignKeyConstraint baseTableName=account, constraintName=FKkm8yb63h4ownvnlrbwnadntyn, referencedTableName=client', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-6::jorge.arevalo (generated)
ALTER TABLE public.movement ADD CONSTRAINT "FKoemeananv9w9qnbcoccbl70a0" FOREIGN KEY (account_id) REFERENCES public.account (id);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-6', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 6, '8:49403a117589006f6da3aa971ccce68c', 'addForeignKeyConstraint baseTableName=movement, constraintName=FKoemeananv9w9qnbcoccbl70a0, referencedTableName=account', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Changeset bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml::1657864844239-7::jorge.arevalo (generated)
ALTER TABLE public.client ADD CONSTRAINT "FKr1e0j10i9v9i52l6tqfa69nj0" FOREIGN KEY (id) REFERENCES public.person (id);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1657864844239-7', 'jorge.arevalo (generated)', 'bank-challenge/globank-management-jorge-arevalo/src/main/resources/db/changelog/changelog-main.xml', NOW(), 7, '8:f6248ed70f3523cc6b0733cb9c9938f3', 'addForeignKeyConstraint baseTableName=client, constraintName=FKr1e0j10i9v9i52l6tqfa69nj0, referencedTableName=person', '', 'EXECUTED', NULL, NULL, '4.9.1', '7864846195');

-- Release Database Lock
UPDATE public.databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

