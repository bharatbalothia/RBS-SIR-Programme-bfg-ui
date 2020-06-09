--------------------------------------------------------
--  File created - Tuesday-August-20-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence SCT_BUNDLE_IDSEQ
--------------------------------------------------------

   CREATE SEQUENCE  "BFG_OWNER"."SCT_BUNDLE_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999 INCREMENT BY 1 START WITH 142765 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SCT_ENTITY_IDSEQ
--------------------------------------------------------

   CREATE SEQUENCE  "BFG_OWNER"."SCT_ENTITY_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1320 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SCT_PAYMENT_IDSEQ
--------------------------------------------------------

   CREATE SEQUENCE  "BFG_OWNER"."SCT_PAYMENT_IDSEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 3313163 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SCT_SCHEDULE_IDSEQ
--------------------------------------------------------

   CREATE SEQUENCE  "BFG_OWNER"."SCT_SCHEDULE_IDSEQ"  MINVALUE 1 MAXVALUE 99999999999999999999999999 INCREMENT BY 1 START WITH 115 NOCACHE  NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table FB_SFGLEGACY_LINK
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."FB_SFGLEGACY_LINK" ("BUNDLE_ID" NUMBER, "DELIVERY_KEY" CHAR(24 BYTE), "ROUTE_KEY" CHAR(24 BYTE), "CREATED" DATE) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  DDL for Table SFG_CHANGE_CONTROL
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."SFG_CHANGE_CONTROL" ("CHANGE_ID" VARCHAR2(255 BYTE), "OPERATION" VARCHAR2(50 BYTE), "STATUS" NUMBER(*,0), "OBJECT_TYPE" VARCHAR2(255 BYTE), "OBJECT_KEY" VARCHAR2(255 BYTE), "CHANGE_USER" VARCHAR2(50 BYTE), "CHANGE_DATE" DATE, "CHANGE_COMMENTS" VARCHAR2(255 BYTE), "APPROVE_USER" VARCHAR2(50 BYTE), "APPROVE_DATE" DATE, "APPROVE_COMMENTS" VARCHAR2(255 BYTE), "ACTION_TYPE" VARCHAR2(255 BYTE), "ACTION_OBJECT" VARCHAR2(255 BYTE), "RESULT_TYPE" VARCHAR2(255 BYTE), "RESULT_OBJECT" VARCHAR2(255 BYTE), "RESULT_META1" VARCHAR2(255 BYTE), "RESULT_META2" VARCHAR2(255 BYTE), "RESULT_META3" VARCHAR2(255 BYTE)) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  DDL for Table SCT_BUNDLE
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."SCT_BUNDLE" ("BUNDLE_ID" NUMBER(*,0), "FILENAME" VARCHAR2(255 BYTE), "REFERENCE" VARCHAR2(255 BYTE), "BTIMESTAMP" DATE, "BTYPE" VARCHAR2(20 BYTE), "ENTITY_ID" NUMBER(*,0), "STATUS" NUMBER(*,0) DEFAULT 0, "ERROR" VARCHAR2(20 BYTE), "WF_ID" NUMBER(*,0), "MESSAGE_ID" NUMBER(*,0), "ISOUTBOUND" NUMBER(*,0), "ISOVERRIDE" NUMBER(*,0) DEFAULT 0, "SERVICE" VARCHAR2(20 BYTE) DEFAULT 'SCT', "DOC_ID" VARCHAR2(255 BYTE), "REPORTED" VARCHAR2(1 BYTE) DEFAULT 'N') SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  DDL for Table SCT_ENTITY
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."SCT_ENTITY" ("ENTITY_ID" NUMBER(*,0), "ENTITY" VARCHAR2(11 BYTE), "SERVICE" VARCHAR2(20 BYTE), "REQUESTORDN" VARCHAR2(255 BYTE), "RESPONDERDN" VARCHAR2(255 BYTE), "SERVICENAME" VARCHAR2(255 BYTE), "REQUESTTYPE" VARCHAR2(255 BYTE), "SNF" NUMBER(*,0) DEFAULT 0, "TRACE" NUMBER(*,0) DEFAULT 0, "DELIVERYNOTIF" NUMBER(*,0) DEFAULT 0, "DELIVERYNOTIFDN" VARCHAR2(255 BYTE), "DELIVERYNOTIFRT" VARCHAR2(255 BYTE), "REQUESTREF" VARCHAR2(255 BYTE), "FILEDESC" VARCHAR2(255 BYTE), "FILEINFO" VARCHAR2(255 BYTE), "TRANSFERDESC" VARCHAR2(255 BYTE), "TRANSFERINFO" VARCHAR2(255 BYTE), "COMPRESSION" NUMBER(*,0), "MAILBOXPATHIN" VARCHAR2(255 BYTE), "MAILBOXPATHOUT" VARCHAR2(255 BYTE), "MQQUEUEIN" VARCHAR2(255 BYTE), "MQQUEUEOUT" VARCHAR2(255 BYTE), "MAXTRANSPERBULK" NUMBER(*,0), "MAXBULKSPERFILE" NUMBER(*,0), "STARTOFDAY" NUMBER(*,0), "ENDOFDAY" NUMBER(*,0), "CDNODE" VARCHAR2(255 BYTE), "IDF_WTOMSGID" VARCHAR2(255 BYTE), "DNF_WTOMSGID" VARCHAR2(255 BYTE), "DVF_WTOMSGID" VARCHAR2(255 BYTE), "SDF_WTOMSGID" VARCHAR2(255 BYTE), "RSF_WTOMSGID" VARCHAR2(255 BYTE), "CDF_WTOMSGID" VARCHAR2(255 BYTE), "MSR_WTOMSGID" VARCHAR2(255 BYTE), "PSR_WTOMSGID" VARCHAR2(255 BYTE), "DRR_WTOMSGID" VARCHAR2(255 BYTE), "RTF_WTOMSGID" VARCHAR2(255 BYTE), "MBP_WTOMSGID" VARCHAR2(255 BYTE), "MQ_HOST" VARCHAR2(255 BYTE), "MQ_PORT" NUMBER(*,0), "MQ_QMANAGER" VARCHAR2(255 BYTE), "MQ_CHANNEL" VARCHAR2(255 BYTE), "MQ_QNAME" VARCHAR2(255 BYTE), "MQ_QBINDING" VARCHAR2(255 BYTE), "MQ_QCONTEXT" VARCHAR2(255 BYTE), "MQ_DEBUG" NUMBER(*,0), "MQ_SSLOPTION" VARCHAR2(50 BYTE), "MQ_SSLCIPHERS" VARCHAR2(255 BYTE), "MQ_SSLSYSTEMCERTID" VARCHAR2(255 BYTE), "MQ_SSLCACERTID" VARCHAR2(255 BYTE), "NONREPUDIATION" NUMBER DEFAULT 0, "ROUTE_REQUESTORDN" VARCHAR2(255 BYTE), "ROUTE_RESPONDERDN" VARCHAR2(255 BYTE), "ROUTE_SERVICE" VARCHAR2(255 BYTE), "ROUTE_REQUESTTYPE" VARCHAR2(255 BYTE), "PAUSE_INBOUND" NUMBER DEFAULT 0, "PAUSE_OUTBOUND" NUMBER DEFAULT 0, "ISDELETED" NUMBER(*,0) DEFAULT 0, "E2ESIGNING" VARCHAR2(50 BYTE), "ENTITY_PARTICIPANT_TYPE" VARCHAR2(20 BYTE), "DIRECT_PARTICIPANT" VARCHAR2(35 BYTE), "MQ_SESSIONTIMEOUT" NUMBER(22,0), "MQ_HEADER" VARCHAR2(255 BYTE)) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  DDL for Table SCT_PAYMENT
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."SCT_PAYMENT" ("PAYMENT_ID" NUMBER(*,0), "MESSAGE_ID" NUMBER(*,0), "WF_ID" NUMBER(*,0), "TYPE" VARCHAR2(20 BYTE), "REFERENCE" VARCHAR2(255 BYTE), "TRANSACTION_ID" VARCHAR2(255 BYTE), "SETTLE_DATE" DATE, "SETTLE_AMT" NUMBER(18,2), "BUNDLE_ID" NUMBER(*,0), "PTIMESTAMP" DATE, "STATUS" NUMBER(*,0) DEFAULT 0, "ISOUTBOUND" NUMBER(*,0), "DOC_ID" VARCHAR2(255 BYTE), "GROUP1" VARCHAR2(255 BYTE), "GROUP2" VARCHAR2(255 BYTE), "PAYMENT_BIC" VARCHAR2(11 BYTE), "ORIGINATING_TX_TYPE" VARCHAR2(10 BYTE)) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  DDL for Table SCT_PAYMENT_ARCHIVE
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" ("PAYMENT_ID" NUMBER(*,0), "MESSAGE_ID" NUMBER(*,0), "WF_ID" NUMBER(*,0), "TYPE" VARCHAR2(20 BYTE), "REFERENCE" VARCHAR2(255 BYTE), "TRANSACTION_ID" VARCHAR2(255 BYTE), "SETTLE_DATE" DATE, "SETTLE_AMT" NUMBER(18,2), "BUNDLE_ID" NUMBER(*,0), "PTIMESTAMP" DATE, "STATUS" NUMBER(*,0) DEFAULT 0, "ISOUTBOUND" NUMBER(*,0), "DOC_ID" VARCHAR2(255 BYTE), "GROUP1" VARCHAR2(255 BYTE), "GROUP2" VARCHAR2(255 BYTE), "PAYMENT_BIC" VARCHAR2(11 BYTE), "ORIGINATING_TX_TYPE" VARCHAR2(10 BYTE)) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  DDL for Table SCT_SCHEDULE
--------------------------------------------------------

  CREATE TABLE "BFG_OWNER"."SCT_SCHEDULE" ("SCHEDULE_ID" NUMBER(*,0), "ENTITY_ID" NUMBER(*,0), "ISWINDOW" NUMBER(*,0), "TIMESTART" NUMBER(*,0), "WINDOWEND" NUMBER(*,0), "WINDOWINTERVAL" NUMBER(*,0), "TRANSTHRESHOLD" NUMBER(*,0), "ACTIVE" NUMBER(*,0) DEFAULT 1, "NEXTRUN" DATE, "LASTRUN" DATE) SEGMENT CREATION IMMEDIATE PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC" ;
--------------------------------------------------------
--  Constraints for Table FB_SFGLEGACY_LINK
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."FB_SFGLEGACY_LINK" ADD CONSTRAINT "FB_SFGLEGY_LINK_PK" PRIMARY KEY ("BUNDLE_ID", "DELIVERY_KEY") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."FB_SFGLEGACY_LINK" MODIFY ("CREATED" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."FB_SFGLEGACY_LINK" MODIFY ("ROUTE_KEY" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."FB_SFGLEGACY_LINK" MODIFY ("DELIVERY_KEY" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."FB_SFGLEGACY_LINK" MODIFY ("BUNDLE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SFG_CHANGE_CONTROL
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SFG_CHANGE_CONTROL" MODIFY ("CHANGE_DATE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SFG_CHANGE_CONTROL" MODIFY ("CHANGE_USER" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SFG_CHANGE_CONTROL" MODIFY ("STATUS" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SFG_CHANGE_CONTROL" MODIFY ("OPERATION" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SFG_CHANGE_CONTROL" MODIFY ("CHANGE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SCT_BUNDLE
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_C02" CHECK (ISOVERRIDE IN (0,1)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_PK" PRIMARY KEY ("BUNDLE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_C01" CHECK (ISOUTBOUND IN (0,1,2)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" MODIFY ("SERVICE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" MODIFY ("ISOVERRIDE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" MODIFY ("ISOUTBOUND" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" MODIFY ("STATUS" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" MODIFY ("BUNDLE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SCT_ENTITY
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_C01" CHECK (SNF IN (0,1)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_C02" CHECK (TRACE IN (0,1)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_C03" CHECK (DELIVERYNOTIF IN (0,1)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_C04" CHECK (ISDELETED in (0,1)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_PK" PRIMARY KEY ("ENTITY_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_U01" UNIQUE ("MQQUEUEOUT") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_U02" UNIQUE ("MAILBOXPATHOUT") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" ADD CONSTRAINT "SCT_ENTITY_U03" UNIQUE ("ENTITY", "SERVICE") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("PAUSE_OUTBOUND" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("PAUSE_INBOUND" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("NONREPUDIATION" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("ENDOFDAY" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("STARTOFDAY" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("MAXBULKSPERFILE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("MAXTRANSPERBULK" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("MAILBOXPATHOUT" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("MAILBOXPATHIN" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("COMPRESSION" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("DELIVERYNOTIF" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("TRACE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("SNF" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("SERVICE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("ENTITY" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_ENTITY" MODIFY ("ENTITY_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SCT_PAYMENT
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_PK" PRIMARY KEY ("PAYMENT_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_U01" UNIQUE ("MESSAGE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_C01" CHECK (ISOUTBOUND IN (0,1,2)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" MODIFY ("ISOUTBOUND" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" MODIFY ("STATUS" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" MODIFY ("PAYMENT_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SCT_PAYMENT_ARCHIVE
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" ADD CONSTRAINT "SCT_PAYMENT_A_C01" CHECK (ISOUTBOUND IN (0,1,2)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" ADD CONSTRAINT "SCT_PAYMENT_A_PK" PRIMARY KEY ("PAYMENT_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" ADD CONSTRAINT "SCT_PAYMENT_A_U01" UNIQUE ("MESSAGE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" MODIFY ("ISOUTBOUND" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" MODIFY ("STATUS" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT_ARCHIVE" MODIFY ("PAYMENT_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SCT_SCHEDULE
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" ADD CONSTRAINT "SCT_SCHEDULE_C01" CHECK (ACTIVE IN (0,1)) ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" ADD CONSTRAINT "SCT_SCHEDULE_PK" PRIMARY KEY ("SCHEDULE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "MFG_DYNAMIC"  ENABLE;
  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" MODIFY ("ACTIVE" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" MODIFY ("ISWINDOW" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" MODIFY ("ENTITY_ID" NOT NULL ENABLE);
  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" MODIFY ("SCHEDULE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table SCT_BUNDLE
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_BUNDLE" ADD CONSTRAINT "SCT_BUNDLE_F01" FOREIGN KEY ("ENTITY_ID") REFERENCES "BFG_OWNER"."SCT_ENTITY" ("ENTITY_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SCT_PAYMENT
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_PAYMENT" ADD CONSTRAINT "SCT_PAYMENT_F01" FOREIGN KEY ("BUNDLE_ID") REFERENCES "BFG_OWNER"."SCT_BUNDLE" ("BUNDLE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SCT_SCHEDULE
--------------------------------------------------------

  ALTER TABLE "BFG_OWNER"."SCT_SCHEDULE" ADD CONSTRAINT "SCT_SCHEDULE_F01" FOREIGN KEY ("ENTITY_ID") REFERENCES "BFG_OWNER"."SCT_ENTITY" ("ENTITY_ID") ENABLE;
--------------------------------------------------------
--  DDL for Trigger SCT_PAYMENT_NEWID
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "BFG_OWNER"."SCT_PAYMENT_NEWID" 
BEFORE INSERT
ON SCT_PAYMENT REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN
 SELECT SCT_PAYMENT_IDSEQ.NEXTVAL INTO :NEW.PAYMENT_ID FROM DUAL;
END;



/
ALTER TRIGGER "BFG_OWNER"."SCT_PAYMENT_NEWID" ENABLE;