connect 'jdbc:derby:/var/glassfish/databases/hv';
alter table ACCOUNTCONNECTION drop column hbciversion;
alter table ACCOUNTCONNECTION add TITLE varchar (255);
describe ACCOUNTCONNECTION;
