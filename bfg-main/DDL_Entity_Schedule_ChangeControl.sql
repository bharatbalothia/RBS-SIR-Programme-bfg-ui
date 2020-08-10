--------------------------------------------------------
--  DDL for Sequence SCT_SCHEDULE_IDSEQ
--------------------------------------------------------

   CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_SCHEDULE_IDSEQ"
   MINVALUE 1
   MAXVALUE 99999999999999999999999999
   INCREMENT BY 1
   START WITH 115
   NOCACHE
   NOORDER
   NOCYCLE;
   
--------------------------------------------------------

--  DDL for Table SCT_SCHEDULE

--------------------------------------------------------

 

  CREATE TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_SCHEDULE"

   (           "SCHEDULE_ID" NUMBER(*,0),

                "ENTITY_ID" NUMBER(*,0),

                "ISWINDOW" NUMBER(*,0),

                "TIMESTART" NUMBER(*,0),

                "WINDOWEND" NUMBER(*,0),

                "WINDOWINTERVAL" NUMBER(*,0),

                "TRANSTHRESHOLD" NUMBER(*,0),

                "ACTIVE" NUMBER(*,0) DEFAULT 1,

                "NEXTRUN" DATE,

                "LASTRUN" DATE,

                "FILETYPE" VARCHAR2(8 BYTE)

   ) SEGMENT CREATION IMMEDIATE

  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING

  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645

  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)

  TABLESPACE "MFG_DYNAMIC" ;
  

   
   
--------------------------------------------------------
--  DDL for Sequence SCT_ENTITY_IDSEQ
--------------------------------------------------------

   CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SCT_ENTITY_IDSEQ"
   MINVALUE 1
   MAXVALUE 9999999999999999999999
   INCREMENT BY 1
   START WITH 1320
   NOCACHE
   NOORDER
   NOCYCLE;   
--------------------------------------------------------

--  DDL for Table SCT_ENTITY

--------------------------------------------------------

 

  CREATE TABLE "RBS_BFG_CUSTOM_OWNER"."SCT_ENTITY"

   (           "ENTITY_ID" NUMBER(*,0),

                "ENTITY" VARCHAR2(11 BYTE),

                "SERVICE" VARCHAR2(20 BYTE),

                "REQUESTORDN" VARCHAR2(255 BYTE),

                "RESPONDERDN" VARCHAR2(255 BYTE),

                "SERVICENAME" VARCHAR2(255 BYTE),

                "REQUESTTYPE" VARCHAR2(255 BYTE),

                "SNF" NUMBER(*,0) DEFAULT 0,

                "TRACE" NUMBER(*,0) DEFAULT 0,

                "DELIVERYNOTIF" NUMBER(*,0) DEFAULT 0,

                "DELIVERYNOTIFDN" VARCHAR2(255 BYTE),

                "DELIVERYNOTIFRT" VARCHAR2(255 BYTE),

                "REQUESTREF" VARCHAR2(255 BYTE),

                "FILEDESC" VARCHAR2(255 BYTE),

                "FILEINFO" VARCHAR2(255 BYTE),

                "TRANSFERDESC" VARCHAR2(255 BYTE),

                "TRANSFERINFO" VARCHAR2(255 BYTE),

                "COMPRESSION" NUMBER(*,0),

                "MAILBOXPATHIN" VARCHAR2(255 BYTE),

                "MAILBOXPATHOUT" VARCHAR2(255 BYTE),

                "MQQUEUEIN" VARCHAR2(255 BYTE),

                "MQQUEUEOUT" VARCHAR2(255 BYTE),

                "MAXTRANSPERBULK" NUMBER(*,0),

                "MAXBULKSPERFILE" NUMBER(*,0),

                "STARTOFDAY" NUMBER(*,0),

                "ENDOFDAY" NUMBER(*,0),

                "CDNODE" VARCHAR2(255 BYTE),

                "IDF_WTOMSGID" VARCHAR2(255 BYTE),

                "DNF_WTOMSGID" VARCHAR2(255 BYTE),

                "DVF_WTOMSGID" VARCHAR2(255 BYTE),

                "SDF_WTOMSGID" VARCHAR2(255 BYTE),

                "RSF_WTOMSGID" VARCHAR2(255 BYTE),

                "CDF_WTOMSGID" VARCHAR2(255 BYTE),

                "MSR_WTOMSGID" VARCHAR2(255 BYTE),

                "PSR_WTOMSGID" VARCHAR2(255 BYTE),

                "DRR_WTOMSGID" VARCHAR2(255 BYTE),

                "RTF_WTOMSGID" VARCHAR2(255 BYTE),

                "MBP_WTOMSGID" VARCHAR2(255 BYTE),

                "MQ_HOST" VARCHAR2(255 BYTE),

                "MQ_PORT" NUMBER(*,0),

                "MQ_QMANAGER" VARCHAR2(255 BYTE),

                "MQ_CHANNEL" VARCHAR2(255 BYTE),

                "MQ_QNAME" VARCHAR2(255 BYTE),

                "MQ_QBINDING" VARCHAR2(255 BYTE),

                "MQ_QCONTEXT" VARCHAR2(255 BYTE),

                "MQ_DEBUG" NUMBER(*,0),

                "MQ_SSLOPTION" VARCHAR2(50 BYTE),

                "MQ_SSLCIPHERS" VARCHAR2(255 BYTE),

                "MQ_SSLSYSTEMCERTID" VARCHAR2(255 BYTE),

                "MQ_SSLCACERTID" VARCHAR2(255 BYTE),

                "NONREPUDIATION" NUMBER DEFAULT 0,

                "ROUTE_REQUESTORDN" VARCHAR2(255 BYTE),

                "ROUTE_RESPONDERDN" VARCHAR2(255 BYTE),

                "ROUTE_SERVICE" VARCHAR2(255 BYTE),

                "ROUTE_REQUESTTYPE" VARCHAR2(255 BYTE),

                "PAUSE_INBOUND" NUMBER DEFAULT 0,

                "PAUSE_OUTBOUND" NUMBER DEFAULT 0,

                "ISDELETED" NUMBER(*,0) DEFAULT 0,

                "E2ESIGNING" VARCHAR2(50 BYTE),

                "ENTITY_PARTICIPANT_TYPE" VARCHAR2(20 BYTE),

                "DIRECT_PARTICIPANT" VARCHAR2(35 BYTE),

                "MQ_SESSIONTIMEOUT" NUMBER(22,0),

                "MQ_HEADER" VARCHAR2(255 BYTE)

   ) SEGMENT CREATION IMMEDIATE

  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING

  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645

  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)

  TABLESPACE "MFG_DYNAMIC" ;   
  
  
--------------------------------------------------------
--  DDL for Sequence SFG_CHANGE_CONTROL_IDSEQ
--------------------------------------------------------  
  
  CREATE SEQUENCE  "RBS_BFG_CUSTOM_OWNER"."SFG_CHANGE_CONTROL_IDSEQ"
    MINVALUE 1
    MAXVALUE 9999999999999999999999999
    INCREMENT BY 1
    START WITH 1000000
    NOCACHE
    NOORDER
    NOCYCLE;
	
	
--------------------------------------------------------

--  DDL for Table SFG_CHANGE_CONTROL

--------------------------------------------------------

 

  CREATE TABLE "RBS_BFG_CUSTOM_OWNER"."SFG_CHANGE_CONTROL"

   (           "CHANGE_ID" VARCHAR2(255 BYTE),

                "OPERATION" VARCHAR2(50 BYTE),

                "STATUS" NUMBER(*,0),

                "OBJECT_TYPE" VARCHAR2(255 BYTE),

                "OBJECT_KEY" VARCHAR2(255 BYTE),

                "CHANGE_USER" VARCHAR2(50 BYTE),

                "CHANGE_DATE" DATE,

                "CHANGE_COMMENTS" VARCHAR2(255 BYTE),

                "APPROVE_USER" VARCHAR2(50 BYTE),

                "APPROVE_DATE" DATE,

                "APPROVE_COMMENTS" VARCHAR2(255 BYTE),

                "ACTION_TYPE" VARCHAR2(255 BYTE),

                "ACTION_OBJECT" VARCHAR2(255 BYTE),

                "RESULT_TYPE" VARCHAR2(255 BYTE),

                "RESULT_OBJECT" VARCHAR2(255 BYTE),

                "RESULT_META1" VARCHAR2(255 BYTE),

                "RESULT_META2" VARCHAR2(255 BYTE),

                "RESULT_META3" VARCHAR2(255 BYTE),
				
				"ENTITY_LOG_ID" VARCHAR2(255)

   ) SEGMENT CREATION IMMEDIATE

  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING

  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645

  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)

  TABLESPACE "MFG_DYNAMIC" ;	
	
	