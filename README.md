# Transaction-Mailer #

This enterprise app is regularly pulling your bank transactions and sending mails with deltas.

### Motivation ###

Use it to stay informed about your bank transactions.

### How do I get set up? ###

* buy a raspberry pi
* install a glassfish server (here to /opt/glassfish)
* to reduce glassfishs memory usage: alter the /config/domain.xml file and set <jvm-options>-Xmx128m</jvm-options>
* to register your email-provider: run /transaction-mailer/src/main/server/javamail/create-java-mail-resource.sh
* to setup a derby embedded db: run /transaction-mailer/src/main/server/db/create-connectionpool.sh
* to setup glassfish as a system service copy /transaction-mailer/src/main/server/glassfish.service to /etc/systemd/system
* configure your transaction-mailer at http://<raspberrypihost>:8080/transaction-mailer/


### Contribution guidelines ###

* Writing tests
* Code review

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact