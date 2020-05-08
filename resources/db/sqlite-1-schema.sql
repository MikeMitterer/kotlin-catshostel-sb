-- noinspection SqlNoDataSourceInspectionForFile

/*-------------------------------------------------------------------------------------------------
Copyright: Mike Mitterer <office@mikemitterer.at>
    Generated for MySQL
-------------------------------------------------------------------------------------------------*/

--
/*
create database webappbase;
*/

-- Shiro - cleanup --------------------------------------------------------------------------------
DROP TABLE IF EXISTS cats;

-- Trigger werden erst in der Trigger-Sektion gelöscht! (Vermeidet Warning)

-- Shiro - tables ---------------------------------------------------------------------------------
CREATE TABLE cats (
    ID      INTEGER PRIMARY KEY AUTOINCREMENT,
	name    VARCHAR(255) 	not null /*REFERENCES userroles(username) ON DELETE CASCADE*/,
	age     INTEGER	        not null
);
DROP INDEX IF EXISTS idx_name ;
CREATE UNIQUE INDEX idx_name on cats(name);

