
cd "${glassfish_home}"/bin

./asadmin delete-jdbc-connection-pool --cascade=true hv-pool

./asadmin \
create-jdbc-connection-pool \
--datasourceclassname org.apache.derby.jdbc.EmbeddedXADataSource \
--restype javax.sql.XADataSource \
--property User=APP:Password=APP:DatabaseName=/var/glassfish/databases/hv:ServerName=localhost:PortNumber=1527:connectionAttributes=\;create\\=true \
hv-pool

./asadmin create-jdbc-resource --connectionpoolid hv-pool hv-jndi

./asadmin ping-connection-pool hv-pool

./asadmin list-jdbc-connection-pools
./asadmin list-jdbc-resources
