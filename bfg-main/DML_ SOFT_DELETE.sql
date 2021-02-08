--------------------------------------------------------
--  DML for SCT_ENTITY
--------------------------------------------------------

  UPDATE "$$TBLUSER$$".SCT_ENTITY
    SET ISDELETED = 1,
	SERVICE = ('DEL_' || ENTITY_ID || '_' || SERVICE),
	MAILBOXPATHOUT = ('DEL_' || ENTITY_ID || '_' || MAILBOXPATHOUT),
	MQQUEUEOUT= ('DEL_' || ENTITY_ID || '_' || MQQUEUEOUT)
    WHERE SERVICE IN ('SDD', 'ROI', 'TRD');