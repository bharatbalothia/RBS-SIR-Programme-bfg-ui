---sequences

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."FB_CHANGE_CONTROL_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999 INCREMENT BY 1 START WITH 1000044 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_AUDIT_ID_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 5901 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_ENTITY_LOG_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1860 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_SCHEDULE_IDSEQ"  MINVALUE 1 MAXVALUE 99999999999999999999999999 INCREMENT BY 1 START WITH 215 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_TRUSTED_CERT_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1421 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_TRUSTED_CERT_LOG_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1570 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SFG_CHANGE_CONTROL_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999 INCREMENT BY 1 START WITH 1000746 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999 INCREMENT BY 1 START WITH 152499 NOCACHE  NOORDER  NOCYCLE;

CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 3916481 NOCACHE  NOORDER  NOCYCLE;

---SCT_AUDIT

CREATE TABLE RBS_BFG_CUSTOM_OWNER.SCT_AUDIT 
   (	ID NUMBER(10,0) DEFAULT RBS_BFG_CUSTOM_OWNER.SCT_AUDIT_ID_SEQ.NEXTVAL NOT NULL ENABLE, 
	PRINCIPAL VARCHAR2(256 BYTE) NOT NULL ENABLE, 
	ACTION_TYPE VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	EVENT_TYPE VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	TYPE VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	OBJECT_NAME VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	ACTION_VALUE VARCHAR2(520 BYTE) NOT NULL ENABLE, 
	ON_NODE VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	CHANGE_ID VARCHAR2(36 BYTE) NOT NULL ENABLE, 
	CREATED_TS TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
	 CONSTRAINT AUDIT_ID_PK PRIMARY KEY (ID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 1048576 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 1048576 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  CREATE INDEX RBS_BFG_CUSTOM_OWNER.ACTION_VALUE_IDX ON RBS_BFG_CUSTOM_OWNER.SCT_AUDIT (ACTION_VALUE) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 1048576 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  CREATE INDEX RBS_BFG_CUSTOM_OWNER.NODE_IDX ON RBS_BFG_CUSTOM_OWNER.SCT_AUDIT (ON_NODE) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 1048576 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  CREATE INDEX RBS_BFG_CUSTOM_OWNER.PRINCIPAL_IDX ON RBS_BFG_CUSTOM_OWNER.SCT_AUDIT (PRINCIPAL) 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 1048576 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  

---SCT_TRUSTED_CERTIFICATE

CREATE TABLE RBS_BFG_CUSTOM_OWNER.SCT_TRUSTED_CERTIFICATE 
   (	CERT_ID VARCHAR2(255 BYTE), 
	CERTIFICATE_NAME VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	SERIAL_NUMBER VARCHAR2(255 BYTE), 
	START_DATE VARCHAR2(10 BYTE), 
	END_DATE VARCHAR2(10 BYTE), 
	THUMBPRINT VARCHAR2(255 BYTE), 
	ISSUER VARCHAR2(255 BYTE), 
	SUBJECT VARCHAR2(255 BYTE), 
	CERTIFICATE BLOB, 
	THUMBPRINT256 VARCHAR2(255 BYTE), 
	 CONSTRAINT SCT_TRUSTED_CERTIFICATE_PK PRIMARY KEY (CERT_ID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC  ENABLE, 
	 CONSTRAINT SCT_TRUSTED_CERTIFICATE_U01 UNIQUE (CERTIFICATE_NAME)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC 
 LOB (CERTIFICATE) STORE AS BASICFILE (
  TABLESPACE BFG_DYNAMIC ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;
  
  
---SCT_TRUSTED_CERTIFICATE_LOG
  
CREATE TABLE RBS_BFG_CUSTOM_OWNER.SCT_TRUSTED_CERTIFICATE_LOG 
   (	CERT_LOG_ID VARCHAR2(255 BYTE), 
	CERT_ID VARCHAR2(255 BYTE),
	CERTIFICATE_NAME VARCHAR2(255 BYTE), 
	SERIAL_NUMBER VARCHAR2(255 BYTE), 
	START_DATE VARCHAR2(10 BYTE), 
	END_DATE VARCHAR2(10 BYTE), 
	THUMBPRINT VARCHAR2(255 BYTE), 
	ISSUER VARCHAR2(255 BYTE), 
	SUBJECT VARCHAR2(255 BYTE), 
	CERTIFICATE BLOB, 
	THUMBPRINT256 VARCHAR2(255 BYTE), 
  CONSTRAINT SCT_TRUSTED_CERTIFICATE_LOG_PK PRIMARY KEY (CERT_LOG_ID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC 
 LOB (CERTIFICATE) STORE AS BASICFILE (
  TABLESPACE BFG_DYNAMIC ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) ;


---FB_CHANGE_CONTROL

CREATE TABLE RBS_BFG_CUSTOM_OWNER.FB_CHANGE_CONTROL 
   (	CHANGE_ID VARCHAR2(255 BYTE), 
	OPERATION VARCHAR2(50 BYTE), 
	STATUS NUMBER(*,0), 
	OBJECT_TYPE VARCHAR2(255 BYTE), 
	OBJECT_KEY VARCHAR2(255 BYTE), 
	CHANGE_USER VARCHAR2(50 BYTE), 
	CHANGE_DATE DATE, 
	CHANGE_COMMENTS VARCHAR2(255 BYTE), 
	APPROVE_USER VARCHAR2(50 BYTE), 
	APPROVE_DATE DATE, 
	APPROVE_COMMENTS VARCHAR2(255 BYTE), 
	ACTION_TYPE VARCHAR2(255 BYTE), 
	ACTION_OBJECT VARCHAR2(255 BYTE), 
	RESULT_TYPE VARCHAR2(255 BYTE), 
	RESULT_OBJECT VARCHAR2(255 BYTE), 
	RESULT_META1 VARCHAR2(255 BYTE), 
	RESULT_META2 VARCHAR2(255 BYTE), 
	RESULT_META3 VARCHAR2(255 BYTE), 
	CERT_LOG_ID VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	 CONSTRAINT FB_CHANGE_CONTROL_F01 FOREIGN KEY (CERT_LOG_ID)
	  REFERENCES RBS_BFG_CUSTOM_OWNER.SCT_TRUSTED_CERTIFICATE_LOG (CERT_LOG_ID) ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  
 
---SCT_ENTITY_LOG
 
   CREATE TABLE RBS_BFG_CUSTOM_OWNER.SCT_ENTITY_LOG 
   (	ENTITY_LOG_ID VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	ENTITY_ID NUMBER(*,0), 
	ENTITY VARCHAR2(11 BYTE) NOT NULL ENABLE, 
	SERVICE VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	REQUESTORDN VARCHAR2(255 BYTE), 
	RESPONDERDN VARCHAR2(255 BYTE), 
	SERVICENAME VARCHAR2(255 BYTE), 
	REQUESTTYPE VARCHAR2(375 BYTE), 
	SNF NUMBER(*,0) DEFAULT 0 NOT NULL ENABLE, 
	TRACE NUMBER(*,0) DEFAULT 0 NOT NULL ENABLE, 
	DELIVERYNOTIF NUMBER(*,0) DEFAULT 0 NOT NULL ENABLE, 
	DELIVERYNOTIFDN VARCHAR2(255 BYTE), 
	DELIVERYNOTIFRT VARCHAR2(255 BYTE), 
	REQUESTREF VARCHAR2(255 BYTE), 
	FILEDESC VARCHAR2(255 BYTE), 
	FILEINFO VARCHAR2(255 BYTE), 
	TRANSFERDESC VARCHAR2(255 BYTE), 
	TRANSFERINFO VARCHAR2(255 BYTE), 
	COMPRESSION NUMBER(*,0) NOT NULL ENABLE, 
	MAILBOXPATHIN VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	MAILBOXPATHOUT VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	MQQUEUEIN VARCHAR2(255 BYTE), 
	MQQUEUEOUT VARCHAR2(255 BYTE), 
	MAXTRANSPERBULK NUMBER(*,0) NOT NULL ENABLE, 
	MAXBULKSPERFILE NUMBER(*,0) NOT NULL ENABLE, 
	STARTOFDAY NUMBER(*,0) NOT NULL ENABLE, 
	ENDOFDAY NUMBER(*,0) NOT NULL ENABLE, 
	CDNODE VARCHAR2(255 BYTE), 
	IDF_WTOMSGID VARCHAR2(255 BYTE), 
	DNF_WTOMSGID VARCHAR2(255 BYTE), 
	DVF_WTOMSGID VARCHAR2(255 BYTE), 
	SDF_WTOMSGID VARCHAR2(255 BYTE), 
	RSF_WTOMSGID VARCHAR2(255 BYTE), 
	CDF_WTOMSGID VARCHAR2(255 BYTE), 
	MSR_WTOMSGID VARCHAR2(255 BYTE), 
	PSR_WTOMSGID VARCHAR2(255 BYTE), 
	DRR_WTOMSGID VARCHAR2(255 BYTE), 
	RTF_WTOMSGID VARCHAR2(255 BYTE), 
	MBP_WTOMSGID VARCHAR2(255 BYTE), 
	MQ_HOST VARCHAR2(255 BYTE), 
	MQ_PORT NUMBER(*,0), 
	MQ_QMANAGER VARCHAR2(255 BYTE), 
	MQ_CHANNEL VARCHAR2(255 BYTE), 
	MQ_QNAME VARCHAR2(255 BYTE), 
	MQ_QBINDING VARCHAR2(255 BYTE), 
	MQ_QCONTEXT VARCHAR2(255 BYTE), 
	MQ_DEBUG NUMBER(*,0), 
	MQ_SSLOPTION VARCHAR2(50 BYTE), 
	MQ_SSLCIPHERS VARCHAR2(255 BYTE), 
	MQ_SSLSYSTEMCERTID VARCHAR2(255 BYTE), 
	MQ_SSLCACERTID VARCHAR2(255 BYTE), 
	NONREPUDIATION NUMBER DEFAULT 0 NOT NULL ENABLE, 
	ROUTE_REQUESTORDN VARCHAR2(255 BYTE), 
	ROUTE_RESPONDERDN VARCHAR2(255 BYTE), 
	ROUTE_SERVICE VARCHAR2(255 BYTE), 
	ROUTE_REQUESTTYPE VARCHAR2(255 BYTE), 
	PAUSE_INBOUND NUMBER DEFAULT 0 NOT NULL ENABLE, 
	PAUSE_OUTBOUND NUMBER DEFAULT 0 NOT NULL ENABLE, 
	ISDELETED NUMBER(*,0) DEFAULT 0, 
	E2ESIGNING VARCHAR2(50 BYTE), 
	ENTITY_PARTICIPANT_TYPE VARCHAR2(20 BYTE), 
	DIRECT_PARTICIPANT VARCHAR2(35 BYTE), 
	MQ_SESSIONTIMEOUT NUMBER(22,0), 
	MQ_HEADER VARCHAR2(255 BYTE), 
	ROUTE_INBOUND NUMBER(*,0), 
	ROUTE_OUTBOUND NUMBER(*,0), 
	INBOUND_DIR NUMBER(*,0), 
	INBOUND_ROUTING_RULE NUMBER(*,0), 
	INBOUND_REQUEST_TYPE VARCHAR2(255 BYTE), 
	IRISH_STEP2 NUMBER(*,0), 
	SCHEDULES VARCHAR2(4000 BYTE), 
	 CONSTRAINT SCT_ENTITY_LOG_C01 CHECK (SNF IN (0,1)) ENABLE, 
	 CONSTRAINT SCT_ENTITY_LOG_C02 CHECK (TRACE IN (0,1)) ENABLE, 
	 CONSTRAINT SCT_ENTITY_LOG_C03 CHECK (DELIVERYNOTIF IN (0,1)) ENABLE, 
	 CONSTRAINT SCT_ENTITY_LOG_C04 CHECK (ISDELETED in (0,1)) ENABLE, 
	 CONSTRAINT SCT_ENTITY_LOG_PK PRIMARY KEY (ENTITY_LOG_ID)
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  
  
---SFG_CHANGE_CONTROL
  
CREATE TABLE RBS_BFG_CUSTOM_OWNER.SFG_CHANGE_CONTROL 
   (	CHANGE_ID VARCHAR2(255 BYTE), 
	OPERATION VARCHAR2(50 BYTE), 
	STATUS NUMBER(*,0), 
	OBJECT_TYPE VARCHAR2(255 BYTE), 
	OBJECT_KEY VARCHAR2(255 BYTE), 
	CHANGE_USER VARCHAR2(50 BYTE), 
	CHANGE_DATE DATE, 
	CHANGE_COMMENTS VARCHAR2(255 BYTE), 
	APPROVE_USER VARCHAR2(50 BYTE), 
	APPROVE_DATE DATE, 
	APPROVE_COMMENTS VARCHAR2(255 BYTE), 
	ACTION_TYPE VARCHAR2(255 BYTE), 
	ACTION_OBJECT VARCHAR2(255 BYTE), 
	RESULT_TYPE VARCHAR2(255 BYTE), 
	RESULT_OBJECT VARCHAR2(255 BYTE), 
	RESULT_META1 VARCHAR2(255 BYTE), 
	RESULT_META2 VARCHAR2(255 BYTE), 
	RESULT_META3 VARCHAR2(255 BYTE), 
	ENTITY_LOG_ID VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	 CONSTRAINT SFG_CHANGE_CONTROL_F01 FOREIGN KEY (ENTITY_LOG_ID)
	  REFERENCES RBS_BFG_CUSTOM_OWNER.SCT_ENTITY_LOG (ENTITY_LOG_ID) ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;

---SCT_SCHEDULE

CREATE TABLE RBS_BFG_CUSTOM_OWNER.SCT_SCHEDULE 
   (	SCHEDULE_ID NUMBER(*,0)  NOT NULL ENABLE,
	ENTITY_ID NUMBER(*,0)  NOT NULL ENABLE,
	ISWINDOW NUMBER(*,0)  NOT NULL ENABLE,
	TIMESTART NUMBER(*,0), 
	WINDOWEND NUMBER(*,0), 
	WINDOWINTERVAL NUMBER(*,0), 
	TRANSTHRESHOLD NUMBER(*,0), 
	ACTIVE NUMBER(*,0) DEFAULT 1  NOT NULL ENABLE,
	NEXTRUN DATE, 
	LASTRUN DATE, 
	FILETYPE VARCHAR2(8 BYTE) NOT NULL ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE BFG_DYNAMIC ;
  
/*
SCT_BUNDLE
*/
  CREATE TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" (	"BUNDLE_ID" NUMBER(*,0), "FILENAME" VARCHAR2(255 BYTE), "REFERENCE" VARCHAR2(255 BYTE), "BTIMESTAMP" DATE, "BTYPE" VARCHAR2(20 BYTE), "ENTITY_ID" NUMBER(*,0), "STATUS" NUMBER(*,0) DEFAULT 0, "ERROR" VARCHAR2(20 BYTE), "WF_ID" NUMBER(*,0), "MESSAGE_ID" NUMBER(*,0), "ISOUTBOUND" NUMBER(*,0), "ISOVERRIDE" NUMBER(*,0) DEFAULT 0, "SERVICE" VARCHAR2(20 BYTE) DEFAULT 'SCT', "DOC_ID" VARCHAR2(255 BYTE), "REPORTED" VARCHAR2(1 BYTE) DEFAULT 'N') SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_BUNDLE_FNAME
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE_FNAME" ON "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ("FILENAME", "SERVICE", "BTYPE", "DOC_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_BUNDLE_PK
--------------------------------------------------------
CREATE UNIQUE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE_PK" ON "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ("BUNDLE_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_BUNDLE_REF
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE_REF" ON "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ("REFERENCE", "STATUS", "ISOUTBOUND", "ENTITY_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_BUNDLE_WFID
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE_WFID" ON "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ("WF_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  Constraints for Table SCT_BUNDLE
--------------------------------------------------------
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_C02" CHECK (ISOVERRIDE IN (0,1)) ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_PK" PRIMARY KEY ("BUNDLE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"  ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_C01" CHECK (ISOUTBOUND IN (0,1,2)) ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" MODIFY ("SERVICE" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" MODIFY ("ISOVERRIDE" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" MODIFY ("ISOUTBOUND" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" MODIFY ("STATUS" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" MODIFY ("BUNDLE_ID" NOT NULL ENABLE)
;
--------------------------------------------------------
--  Ref Constraints for Table SCT_BUNDLE
--------------------------------------------------------
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_F01" FOREIGN KEY ("ENTITY_ID") REFERENCES "RBS_BFG_CUSTOM_OWNER"."SCT_ENTITY" ("ENTITY_ID") ENABLE
;

/*
SCT_PAYMENT
*/
--------------------------------------------------------
--  DDL for Table SCT_PAYMENT
--------------------------------------------------------
CREATE TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" (	"PAYMENT_ID" NUMBER(*,0), "MESSAGE_ID" NUMBER(*,0), "WF_ID" NUMBER(*,0), "TYPE" VARCHAR2(20 BYTE), "REFERENCE" VARCHAR2(255 BYTE), "TRANSACTION_ID" VARCHAR2(255 BYTE), "SETTLE_DATE" DATE, "SETTLE_AMT" NUMBER(18,2), "BUNDLE_ID" NUMBER(*,0), "PTIMESTAMP" DATE, "STATUS" NUMBER(*,0) DEFAULT 0, "ISOUTBOUND" NUMBER(*,0), "DOC_ID" VARCHAR2(255 BYTE), "GROUP1" VARCHAR2(255 BYTE), "GROUP2" VARCHAR2(255 BYTE), "PAYMENT_BIC" VARCHAR2(11 BYTE), "ORIGINATING_TX_TYPE" VARCHAR2(10 BYTE)) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_BUNDLE
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_BUNDLE" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ("BUNDLE_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_GROUPS
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_GROUPS" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ("GROUP1", "GROUP2") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_MSGID
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_MSGID" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ("MESSAGE_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_PK
--------------------------------------------------------
CREATE UNIQUE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_PK" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ("PAYMENT_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_SETTLE
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_SETTLE" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ("SETTLE_DATE", "TYPE") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC" 
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_WF
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_WF" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ("WF_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  Constraints for Table SCT_PAYMENT
--------------------------------------------------------
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_PK" PRIMARY KEY ("PAYMENT_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"  ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_U01" UNIQUE ("MESSAGE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"  ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_C01" CHECK (ISOUTBOUND IN (0,1,2)) ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" MODIFY ("ISOUTBOUND" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" MODIFY ("STATUS" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" MODIFY ("PAYMENT_ID" NOT NULL ENABLE)
;
--------------------------------------------------------
--  Ref Constraints for Table SCT_PAYMENT
--------------------------------------------------------
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_F01" FOREIGN KEY ("BUNDLE_ID") REFERENCES "RBS_BFG_CUSTOM_OWNER"."SCT_BUNDLE" ("BUNDLE_ID") ENABLE
;

/*
Trigger SCT_PAYMENT_NEWID
*/
--------------------------------------------------------
--  DDL for Trigger SCT_PAYMENT_NEWID
--------------------------------------------------------
CREATE OR REPLACE TRIGGER "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_NEWID" 
BEFORE INSERT ON SCT_PAYMENT REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW 
BEGIN 
SELECT SCT_PAYMENT_IDSEQ.NEXTVAL INTO :NEW.PAYMENT_ID FROM DUAL;
END;
/
ALTER TRIGGER "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_NEWID" ENABLE
;
  
  
/*
SCT_PAYMENT_ARCHIVE
*/
--------------------------------------------------------
--  DDL for Table SCT_PAYMENT_ARCHIVE
--------------------------------------------------------
CREATE TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" (	"PAYMENT_ID" NUMBER(*,0), "MESSAGE_ID" NUMBER(*,0), "WF_ID" NUMBER(*,0), "TYPE" VARCHAR2(20 BYTE), "REFERENCE" VARCHAR2(255 BYTE), "TRANSACTION_ID" VARCHAR2(255 BYTE), "SETTLE_DATE" DATE, "SETTLE_AMT" NUMBER(18,2), "BUNDLE_ID" NUMBER(*,0), "PTIMESTAMP" DATE, "STATUS" NUMBER(*,0) DEFAULT 0, "ISOUTBOUND" NUMBER(*,0), "DOC_ID" VARCHAR2(255 BYTE), "GROUP1" VARCHAR2(255 BYTE), "GROUP2" VARCHAR2(255 BYTE), "PAYMENT_BIC" VARCHAR2(11 BYTE), "ORIGINATING_TX_TYPE" VARCHAR2(10 BYTE)) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_A_BUNDLE
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_A_BUNDLE" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ("BUNDLE_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_A_MSGID
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_A_MSGID" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ("MESSAGE_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_A_PK
--------------------------------------------------------
CREATE UNIQUE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_A_PK" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ("PAYMENT_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_A_SETTLE
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_A_SETTLE" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ("SETTLE_DATE", "TYPE") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  DDL for Index SCT_PAYMENT_A_WF
--------------------------------------------------------
CREATE INDEX "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_A_WF" ON "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ("WF_ID") PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"
;
--------------------------------------------------------
--  Constraints for Table SCT_PAYMENT_ARCHIVE
--------------------------------------------------------
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ADD CONSTRAINT "SCT_PAYMENT_A_C01" CHECK (ISOUTBOUND IN (0,1,2)) ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ADD CONSTRAINT "SCT_PAYMENT_A_PK" PRIMARY KEY ("PAYMENT_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"  ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" ADD CONSTRAINT "SCT_PAYMENT_A_U01" UNIQUE ("MESSAGE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "BFG_DYNAMIC"  ENABLE
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" MODIFY ("ISOUTBOUND" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" MODIFY ("STATUS" NOT NULL ENABLE)
;
ALTER TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_PAYMENT_ARCHIVE" MODIFY ("PAYMENT_ID" NOT NULL ENABLE)
;



