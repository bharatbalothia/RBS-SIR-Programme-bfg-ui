# IBM Sterling Release Notes

## SIR_BFGUI_v1.0-0

This will deploy:

- v1.0-0

## Deployment Schedule of Events

### Bundle Upload and Unpack

1. store unzipped deployment bundle on windows vcs file system
2. using winscp upload deployment bundle to environment node 1 /opt/ibm/media/bundle directory
3. repeat step 2 against all nodes

## Deployment

1. SSH onto SBI node 1
2. cd to the bundle directory created previously (e.g. /opt/ibm/media/bundle) 
3. cd to SIR_BGUI_v1.0-0/IBM/sbi/scripts/
4. execute 'chmod +x ibm_deploy_BFGUI-1.0-0.sh'
5. execute the deployment script './ibm_deploy_BFGUI-1.0-0.sh'
6. Accept or update the default bundle home directory (default is /opt/ibm/media/bundle)
7. Accept or update the default SBI install directory (default is /opt/ibm/sbi/install)
8. Node1 only, Enter the username and password for a SBI admin user (required to invoke REST APIs for custom property deployment)
9. The script will now run for some minutes.
10. Repeat all steps for subsequent SBI nodes.



