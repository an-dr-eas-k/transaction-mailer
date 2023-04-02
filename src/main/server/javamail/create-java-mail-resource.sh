
jndi_name="mail/hv"

# dir=$(cygpath . -wa)

read -p "mailhost>" mailhost
read -p "fromaddress>" fromaddress
read -p "mailuser>" mailuser
read -p "passwd>" passwd

#echo -n > pw-file

#printf "AS_ADMIN_PASSWORD=%s\n" $passwd >> pw-file
#printf "AS_ADMIN_USERPASSWORD=%s\n" $passwd >> pw-file

cd "${glassfish_home}"/bin

./asadmin delete-javamail-resource ${jndi_name}

#./asadmin \
#create-javamail-resource \
#--mailhost smtp.gmail.com \
#--mailuser name@gmail.com \
#--fromaddress name@gmail.com \
#--property mail.smtp.socketFactory.port=465:mail.smtp.port=465:mail.smtp.socketFactory.fallback=false:mail.smtp.auth=true:mail.smtp.password=${passwd}:mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory \
#${jndi_name}

./asadmin \
create-javamail-resource \
--mailhost ${mailhost} \
--mailuser ${mailuser} \
--fromaddress "tm <${fromaddress}>" \
--property mail.smtp.auth=true:mail.smtp.password=${passwd} \
${jndi_name}


./asadmin list-javamail-resources

