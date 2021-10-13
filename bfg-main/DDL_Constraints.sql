--------------------------------------------------------
--  Constraints for Table SCT_ENTITY
--------------------------------------------------------

  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    ADD CONSTRAINT "SCT_ENTITY_LOG_C01" CHECK (SNF IN (0,1)) ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    ADD CONSTRAINT "SCT_ENTITY_LOG_C02" CHECK (TRACE IN (0,1)) ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    ADD CONSTRAINT "SCT_ENTITY_LOG_C03" CHECK (DELIVERYNOTIF IN (0,1)) ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    ADD CONSTRAINT "SCT_ENTITY_LOG_C04" CHECK (ISDELETED in (0,1)) ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    ADD CONSTRAINT "SCT_ENTITY_LOG_PK" PRIMARY KEY ("ENTITY_LOG_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "$$TBLSPACE$$"  ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("PAUSE_OUTBOUND" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("PAUSE_INBOUND" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("NONREPUDIATION" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("ENDOFDAY" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("STARTOFDAY" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("MAXBULKSPERFILE" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("MAXTRANSPERBULK" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("MAILBOXPATHOUT" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("MAILBOXPATHIN" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("COMPRESSION" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("DELIVERYNOTIF" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("TRACE" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("SNF" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("SERVICE" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("ENTITY" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SCT_ENTITY_LOG"
    MODIFY ("ENTITY_LOG_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table SFG_CHANGE_CONTROL
--------------------------------------------------------

  ALTER TABLE "$$TBLUSER$$"."SFG_CHANGE_CONTROL"
    MODIFY ("ENTITY_LOG_ID" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."SFG_CHANGE_CONTROL"
    ADD CONSTRAINT "SFG_CHANGE_CONTROL_F01" FOREIGN KEY ("ENTITY_LOG_ID") REFERENCES "$$TBLUSER$$"."SCT_ENTITY_LOG" ("ENTITY_LOG_ID") ENABLE;

--------------------------------------------------------
--  Constraints for Table SCT_SCHEDULE
--------------------------------------------------------

  ALTER TABLE "$$TBLUSER$$"."SCT_SCHEDULE"
    MODIFY ("FILETYPE" NOT NULL ENABLE)

--------------------------------------------------------
--  Constraints for Table SCT_TRUSTED_CERTIFICATE
--------------------------------------------------------

  ALTER TABLE "$$TBLUSER$$"."SCT_TRUSTED_CERTIFICATE"
    ADD CONSTRAINT "SCT_TRUSTED_CERTIFICATE_PK" PRIMARY KEY ("CERT_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "$$TBLSPACE$$"  ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_TRUSTED_CERTIFICATE"
    ADD CONSTRAINT "SCT_TRUSTED_CERTIFICATE_U01" UNIQUE ("CERTIFICATE_NAME") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "$$TBLSPACE$$"  ENABLE;
  ALTER TABLE "$$TBLUSER$$"."SCT_TRUSTED_CERTIFICATE"
    MODIFY ("CERTIFICATE_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table SCT_TRUSTED_CERTIFICATE_LOG
--------------------------------------------------------

  ALTER TABLE "$$TBLUSER$$"."SCT_TRUSTED_CERTIFICATE_LOG"
    ADD CONSTRAINT "SCT_TRUSTED_CERTIFICATE_LOG_PK" PRIMARY KEY ("CERT_LOG_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "$$TBLSPACE$$"  ENABLE;

--------------------------------------------------------
--  Constraints for Table FB_CHANGE_CONTROL
--------------------------------------------------------

  ALTER TABLE "$$TBLUSER$$"."FB_CHANGE_CONTROL"
    MODIFY ("CERT_LOG_ID" NOT NULL ENABLE);
  ALTER TABLE "$$TBLUSER$$"."FB_CHANGE_CONTROL"
    ADD CONSTRAINT "FB_CHANGE_CONTROL_F01" FOREIGN KEY ("CERT_LOG_ID") REFERENCES "$$TBLUSER$$"."SCT_TRUSTED_CERTIFICATE_LOG" ("CERT_LOG_ID") ENABLE;