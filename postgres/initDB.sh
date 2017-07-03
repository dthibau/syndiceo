#/bin/sh

# Wait for PG to be ready
until psql -c "select version()" &> /dev/null
do
    echo "waiting for postgres container..."
    sleep 2
done

createdb syndiceo
psql -c "ALTER USER \"postgres\" WITH PASSWORD 'postgres';"
psql syndiceo < syndiceo.backup
psql syndiceo < migration.sql
