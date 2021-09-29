#!/bin/bash
############################################################
# ibm ltd (C)2021
# sabrex ltd (C)2021
# ibm_deploy_BFGU_v1.0-0.sh
#
############################################################
#break if fail
set -e
#set log filename
log=ibm_deploy_BFGUI_v1.0-0.log
echo "START Deploying...SIR BFG v1.0-0 Bundle" >>$log
# append date to log file
date >> $log

#set vars
BUNDLE_TYPE="IBM"
BUNDLE_TAG="SIR_BFGUI_v1.0-0"
CMD_IP=import.sh
BACKUP_ID="bak_BFGUI_100"
FULL_DEPLOY="Y"

#set defaults
DEFAULT_SIINSTALLPATH="/opt/ibm/sbi/install"
DEFAULT_BUNDLE_DIR=/opt/ibm/media/bundle

#************************************** 
#Set to bundle....... 
#************************************** 

read -p "Bundle Directory: (default is $DEFAULT_BUNDLE_DIR) " -e PARAM_BUNDLE_DIR
if [ -z "$PARAM_BUNDLE_DIR" ]
then
	BUNDLE_DIR=$DEFAULT_BUNDLE_DIR
else
	echo "Setting Bundle Directory to ${PARAM_BUNDLE_DIR}"
	BUNDLE_DIR=$PARAM_BUNDLE_DIR
fi

read -p "SI Installation Path: (default is $DEFAULT_SIINSTALLPATH) " -e PARAM_SIINSTALLPATH
if [ -z "$PARAM_SIINSTALLPATH" ]
then
	SIINSTALLPATH=$DEFAULT_SIINSTALLPATH
else
	echo "Setting SI Installation Path to ${PARAM_SIINSTALLPATH}"
	SIINSTALLPATH=$PARAM_SIINSTALLPATH
fi

#Get the nodename and number
NODENAME=$((grep "^NODE_NAME" | cut -d= -f2) <${SIINSTALLPATH}/properties/sandbox.cfg)
NODENUM=$(echo $NODENAME | cut -c5)

echo ""
echo "Running deployment on ${NODENAME} [number=${NODENUM}]"
echo ""

#************************************** 
#Bundle details....... 
#**************************************
BUNDLE_SET=${BUNDLE_DIR}/${BUNDLE_TAG}/${BUNDLE_TYPE}
echo "Bundle set: ${BUNDLE_SET}"
echo "Bundle set: ${BUNDLE_SET}" >>$log

#************************************** 
#Start of Deployment....... 
#**************************************
echo "Running Deployment Scripts"
echo "Running Deployment Scripts" >>$log
                                            	
#Node1 only
if [[ "$NODENUM" -eq "1" ]]
then
	
	read -p "SBI Admin Username (no default): " -e PARAM_SBIADMINUSER
	if [ -z "$PARAM_SBIADMINUSER" ]
	then
		echo "Error: SBI Admin Username not provided"
		exit 1
	fi
	read -sp "SBI Admin Password (no default): " -e PARAM_SBIADMINPASS
	if [ -z "$PARAM_SBIADMINPASS" ]
	then
		echo "Error: SBI Admin Password not provided"
		exit 1
	fi
fi

#************************************** 
#Insert bfgui.properties to the database - node1 only
#**************************************
if [[ "$NODENUM" -eq "1" ]]
then

	echo "*** Inserting bfgui.properties into the database ***"
	echo "*** Inserting bfgui.properties into the database ***" >>$log

	base64 ${BUNDLE_SET}/sbi/properties/filesystem/bfgui.properties >> ${BUNDLE_SET}/sbi/properties/filesystem/bfgui.properties.json
	sed -i '1s/^/{\n"description": "bfgui.properties",\n"propertyFileContent": "/' ${BUNDLE_SET}/sbi/properties/filesystem/bfgui.properties.json
	sed -i '$ a\",\n"propertyFilePrefix": "bfgui"\n}' ${BUNDLE_SET}/sbi/properties/filesystem/bfgui.properties.json
	curl --insecure -u ${PARAM_SBIADMINUSER}:${PARAM_SBIADMINPASS} -i -H 'Accept:application/json' -H 'Content-Type:application/json' -d @${BUNDLE_SET}/sbi/properties/filesystem/bfgui.properties.json -X POST https://localhost:7075/B2BAPIs/svc/propertyfiles/

fi

#************************************** 
#Copy sfgerror.properties to the database - node1 only
#**************************************

if [[ "$NODENUM" -eq "1" ]]
then
	echo "*** Copying sfgerror.properties to the database ***"
	echo "*** Copying sfgerror.properties to the database ***" >>$log

	base64 ${SIINSTALLPATH}/properties/sfgerror.properties >> ${BUNDLE_SET}/sbi/properties/filesystem/sfgerror.properties.json
	sed -i '1s/^/{\n"description": "sfgerror.properties",\n"propertyFileContent": "/' ${BUNDLE_SET}/sbi/properties/filesystem/sfgerror.properties.json
	sed -i '$ a\",\n"propertyFilePrefix": "sfgerror"\n}' ${BUNDLE_SET}/sbi/properties/filesystem/sfgerror.properties.json
	curl --insecure -u ${PARAM_SBIADMINUSER}:${PARAM_SBIADMINPASS} -i -H 'Accept:application/json' -H 'Content-Type:application/json' -d @${BUNDLE_SET}/sbi/properties/filesystem/sfgerror.properties.json -X POST https://localhost:7075/B2BAPIs/svc/propertyfiles/

fi


#************************************** 
#Removing properties from filesystem property files - all nodes
#**************************************
echo "*** Removing properties from filesystem property files ***"
echo "*** Removing properties from filesystem property files ***" >>$log

cp ${SIINSTALLPATH}/properties/sct.properties ${SIINSTALLPATH}/properties/sct.properties.${BACKUP_ID}
cp ${SIINSTALLPATH}/properties/gpl.properties ${SIINSTALLPATH}/properties/gpl.properties.${BACKUP_ID}


while read F ; do
        pKey=$(echo $F | cut -d= -f1)
        if ! [[ $pKey =~ ^# ]] && [[ $pKey =~ [:alnum:] ]]; then
                if grep -R "^[#]*\s*${pKey}=.*" ${SIINSTALLPATH}/properties/sct.properties > /dev/null; then
                        echo "Commenting out ${pKey} in sct.properties"
                        sed -i "/^${pKey}=/a #Moved ${pKey} to bfgui.properties" ${SIINSTALLPATH}/properties/sct.properties
                        sed -i "s/^\(${pKey}=\)/#\1/g" ${SIINSTALLPATH}/properties/sct.properties
                fi
                if grep -R "^[#]*\s*${pKey}=.*" ${SIINSTALLPATH}/properties/gpl.properties > /dev/null; then
                        echo "Commenting out ${pKey} in gpl.properties"
                        sed -i "/^${pKey}=/a #Moved ${pKey} to bfgui.properties" ${SIINSTALLPATH}/properties/gpl.properties
                        sed -i "s/^\(${pKey}=\)/#\1/g" ${SIINSTALLPATH}/properties/gpl.properties
                fi
        fi
done < ${BUNDLE_SET}/sbi/properties/filesystem/bfgui.properties

echo "*** Removing sfgerror.properties from disk ***"
echo "*** Removing sfgerror.properties from disk ***" >>$log
mv ${SIINSTALLPATH}/properties/sfgerror.properties ${SIINSTALLPATH}/properties/sfgerror.properties.${BACKUP_ID}

#************************************** 
#Append JDBC REST service settings
#**************************************
if [[ "$FULL_DEPLOY" -eq "Y" ]]
then
	echo "*** Adding JDBC settings for REST service ***"
	echo "*** Adding JDBC settings for REST service ***" >>$log
	cp ${SIINSTALLPATH}/properties/jdbc_customer.properties.in ${SIINSTALLPATH}/properties/jdbc_customer.properties.in.${BACKUP_ID}
	cat ${BUNDLE_SET}/sbi/properties/filesystem/jdbc_customer.properties.in >> ${SIINSTALLPATH}/properties/jdbc_customer.properties.in
	
	#copy in the settings for an existing pool so SBI will start - environment-config will update this later
	JDBC_URL=$((grep "^BFG_DYNAMIC.url" | cut -d= -f2) <${SIINSTALLPATH}/properties/jdbc_customer.properties)
	JDBC_CATALOG=$((grep "^BFG_DYNAMIC.catalog" | cut -d= -f2) <${SIINSTALLPATH}/properties/jdbc_customer.properties)
	JDBC_SCHEMA=$((grep "^BFG_DYNAMIC.schema" | cut -d= -f2) <${SIINSTALLPATH}/properties/jdbc_customer.properties)
	JDBC_USER=$((grep "^BFG_DYNAMIC.user" | cut -d= -f2) <${SIINSTALLPATH}/properties/jdbc_customer.properties)
	JDBC_PASS=$((grep "^BFG_DYNAMIC.password" | cut -d= -f2) <${SIINSTALLPATH}/properties/jdbc_customer.properties)

	sed -i "s|^[#]*\s*BFG_DYNAMIC_REST.url=.*|BFG_DYNAMIC_REST.url=JDBC_URL|" $SIINSTALLPATH/properties/jdbc_customer.properties.in
	sed -i "s|^[#]*\s*BFG_DYNAMIC_REST.catalog=.*|BFG_DYNAMIC_REST.catalog=JDBC_CATALOG|" $SIINSTALLPATH/properties/jdbc_customer.properties.in
	sed -i "s|^[#]*\s*BFG_DYNAMIC_REST.schema=.*|BFG_DYNAMIC_REST.schema=JDBC_SCHEMA|" $SIINSTALLPATH/properties/jdbc_customer.properties.in
	sed -i "s|^[#]*\s*BFG_DYNAMIC_REST.user=.*|BFG_DYNAMIC_REST.user=JDBC_USER|" $SIINSTALLPATH/properties/jdbc_customer.properties.in
	sed -i "s|^[#]*\s*BFG_DYNAMIC_REST.password=.*|BFG_DYNAMIC_REST.password=JDBC_PASSWORD|" $SIINSTALLPATH/properties/jdbc_customer.properties.in
	
fi

#************************************** 
#Deploy SSO-FORWARDER
#**************************************
if [[ "$FULL_DEPLOY" -eq "Y" ]]
then
	echo "*** Deploying SSO-FORWARDER ***"
	echo "*** Deploying SSO-FORWARDER ***" >>$log
	
	#make backups
	cp ${SIINSTALLPATH}/properties/sandbox.cfg ${SIINSTALLPATH}/properties/sandbox.cfg.${BACKUP_ID}
	cp ${SIINSTALLPATH}/properties/customer_overrides.properties.in ${SIINSTALLPATH}/properties/customer_overrides.properties.in.${BACKUP_ID}
	
	#append the settings to existing files
	cat ${BUNDLE_SET}/sbi/properties/filesystem/sandbox.cfg >> ${SIINSTALLPATH}/properties/sandbox.cfg
	cat ${BUNDLE_SET}/sbi/properties/filesystem/pages.properties_customer_ext >> ${SIINSTALLPATH}/properties/pages.properties_customer_ext 
	
	#comment out filegateway_eventlinks.FB_SFG_BUNDLE_LINK
	sed -i "s/^\(filegateway_eventlinks.FB_SFG_BUNDLE_LINK\)/#\1/g" ${SIINSTALLPATH}/properties/gpl.properties
	#insert the new setting after the commented out setting
	awk -v file2="${BUNDLE_SET}/sbi/properties/filesystem/customer_overrides.properties.in" '{ print }found { next }/#filegateway_eventlinks.FB_SFG_BUNDLE_LINK=/ { system("cat " file2); found=1 }' ${SIINSTALLPATH}/properties/customer_overrides.properties.in > ${SIINSTALLPATH}/properties/customer_overrides.properties.tmp_BFGUI_100
	mv ${SIINSTALLPATH}/properties/customer_overrides.properties.tmp_BFGUI_100 ${SIINSTALLPATH}/properties/customer_overrides.properties.in
	
	#deploy the sso-forwarder JSP
	${SIINSTALLPATH}/bin/InstallService.sh ${BUNDLE_SET}/sbi/services/sso-forwarder.jar
	
fi

#************************************** 
#Deploy RESTService WAR
#**************************************
if [[ "$FULL_DEPLOY" -eq "Y" ]]
then
	echo "*** Deploying RESTService WAR file ***"
	echo "*** Deploying RESTService WAR file ***" >>$log
	
	WAR_DIR="/opt/ibm/custom-wars/"
	LOG_DIR="${SIINSTALLPATH}/logs/bfgui/"
	
	if [ -d "$WAR_DIR" ]; then
  		echo "${WAR_DIR} already exists"
  	else
  		mkdir ${WAR_DIR}
	fi
	if [ -d "$LOG_DIR" ]; then
  		echo "${LOG_DIR} already exists"
  	else
  		mkdir ${LOG_DIR}
	fi
	cp ${BUNDLE_SET}/sbi/war/BFGUIRestSupport.war ${WAR_DIR}
	
fi

#************************************** 
#Import SBI Exports....... only for node1
#**************************************
if [[ "$FULL_DEPLOY" -eq "Y" ]]
then
	if [[ "$NODENUM" -eq "1" ]]
	then
		echo "Import SBI exports...."
		echo "Import SBI exports....">>$log
		$SIINSTALLPATH/tp_import/$CMD_IP -input $BUNDLE_SET/sbi/exports/SIR_BFGUI_v1.0-0.xml -update -passphrase password -resourcetag SIR_BFGUI_v1.0-0  -noLocks
	fi
fi


#************************************** 
#ReStart Environment....... 
#**************************************
echo "*** RESTART ENVIRONMENT ***"
echo "*** RESTART ENVIRONMENT ***" >>$log
${SIINSTALLPATH}/bin/hardstop.sh
cd ${SIINSTALLPATH}/bin
./setupfiles.sh
${SIINSTALLPATH}/bin/run.sh
     

#************************************** 
#Deployment Complete....... 
#**************************************
echo "COMPLETED Deploying...SIR BFGUI v1.0-0 bundle"
echo "COMPLETED Deploying...SIR BFGUI v1.0-0 Bundle" >>$log
                                                

