
jndi_name="mail/hv"

dir=$(cygpath . -wa)

read -p "passwd>" passwd

# printf "AS_ADMIN_PASSWORD=%s\n" $passwd > pw-file

cd "${glassfish_home}"/bin

./asadmin delete-javamail-resource ${jndi_name}

./asadmin \
create-javamail-resource \
--mailhost smtp.gmail.com \
--mailuser andreas.kirschner@gmail.com \
--fromaddress andreas.kirchner@gmail.com \
--property mail.smtp.socketFactory.port=465:mail.smtp.port=465:mail.smtp.socketFactory.fallback=false:mail.smtp.auth=true:mail.smtp.password=${passwd}:mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory \
${jndi_name}

./asadmin list-javamail-resources

