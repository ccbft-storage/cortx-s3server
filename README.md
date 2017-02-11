# For now dev centric readme

# Updation to nginx.conf.sample -- Added client_max_body_size to have max body size as 5GB

## How to install clang-format
Install clang-format for code formatting.
```sh
cd ~/Downloads/
wget http://llvm.org/releases/3.8.0/clang+llvm-3.8.0-linux-x86_64-centos6.tar.xz
tar -xvJf clang+llvm-3.8.0-linux-x86_64-centos6.tar.xz
sudo ln -s ~/Downloads/clang+llvm-3.8.0-linux-x86_64-centos6/bin/clang-format /bin/clang-format
```

## How to install git-clang-format
Install git-clang-format to run clang-format only on new changes.
```sh
cd ~/Downloads/
wget https://raw.githubusercontent.com/llvm-mirror/clang/master/tools/clang-format/git-clang-format
chmod +x git-clang-format
sudo cp git-clang-format /usr/bin
git config --global clangFormat.style 'Google'
```

## How to use git clang-format
Once you are ready with your changes to be committed, use below sequence of commands:
```sh
git clang-format --diff <files>   //prints the changes clang-format would make in next command
git clang-format -f <files>       //makes formatting changes in specified files
git add <files>
git commit
```

## How to inform clang-format to ignore code for formatting
If you don't want clang-format to work on a section of code then surround it
with `// clang-format off` and `// clang-format on`
```cpp

#include <iostream>
int main() {
// clang-format off
  std::cout << "Hello world!";
  return 0;
// clang-format on
}

```

## How to Build & Install S3 server, Auth server, UTs & third party libs?
Build steps for Dev environment and for release environment differ slightly.
In case of Dev, we locally build the mero source and use the mero libs from
the source code location. Whereas in case of Release, we assume that mero rpms
are pre-installed and use mero libs from standard location.

Steps for Dev environment:
```sh
./refresh_thirdparty.sh
./rebuildall.sh --no-mero-rpm
```
The `./refresh_thirdparty.sh` command refreshes the third party source code.
It will undo any changes made in third party submodules source code and will
clone missing submodules. Normally after a fresh repo clone, this command
needs to be executed only once.

The `./rebuildall.sh --no-mero-rpm` command will build third party libs, S3
server, Auth server, UTs etc. It will also install S3 server, Auth server &
third party libs at `/opt/seagate` location. Note the option `--no-mero-rpm`
passed to the command. It informs the script that mero source needs to be built
and mero libs from the source code location would be used.
Normally third party libs needs to be built only once after fresh repo clone.
To skip building third party libs on subsequent runs of `./rebuildall.sh`,use
`--no-thirdparty-build` option.
```sh
./rebuildall.sh --no-mero-rpm --no-thirdparty-build
```

Steps for Release environment:
Make sure mero rpms are installed on the build machine before executing
below commands.
```sh
./refresh_thirdparty.sh
./rebuildall.sh
```

## How to run auth server (this current assumes all dependencies are on same local VM)
```sh
sudo systemctl start s3authserver
```

## How to stop auth server (this current assumes all dependencies are on same local VM)
```sh
sudo systemctl stop s3authserver
```

## How to start/stop single instance of S3 server in Dev environment for testing?
Execute below command from `s3server` top level directory. Before executing below
commands, make sure that S3 Server, Auth server, third party libs etc are  built
& installed using `./rebuildall.sh --no-mero-rpm` command. Also make sure S3 Auth
server and Mero services are up & running.
```sh
sudo ./dev-starts3.sh
```

To stop S3 server in Dev environment, use below command
```sh
sudo ./dev-stops3.sh
```

## How to run Auth server tests?
```sh
cd auth
mvn test
cd -
```

## How to run S3 server Unit tests in Dev environment?
```sh
./runalltest.sh --no-mero-rpm --no-st-run
```
Above command runs S3 server UTs. Note the option `--no-mero-rpm` passed
to the command. It informs the script to use mero libs from the source code
location at the run time. In case of Release environment, simply skip passing
the option to the script.

## How to run S3 server standard (ST + UT) tests?
Python virtual env needs to be setup to run the STs. This is a one time step
after fresh repo clone.
```sh
cd st/clitests
./setup.sh
cd ../../
```
The `./setup.sh` script will display few entries to be added to the `/etc/hosts`
file. Go ahead and add those entries.
If you are not running as root then add user to /etc/sudoers file as below:
```sh
sudo visudo
# Find the line `root ALL=(ALL) ALL`, add following line after current line:
<user_name> ALL=(ALL) NOPASSWD:ALL
# Warning: This gives super user privilege to all commands when invoked as sudo.

# Now add s3server binary path to sudo secure path
# Find line with variable `secure_path`, append below to the varible
:/opt/seagate/s3/bin
```

Now setup to run STs is complete. Other details of ST setup can be found at
`st/clitests/readme`.
Use below command to run (ST + UT) tests in Dev environment.
```sh
./runalltest.sh --no-mero-rpm
```
In case of Release environment, simply skip passing the option `--no-mero-rpm` to
the script.

## How to run single s3 service(this current assumes all dependencies are on same local VM)
```sh
sudo /opt/seagate/s3/starts3.sh
```

## How to run multiple instances of s3 service(this currently assumes all dependencies are on same local VM)
```sh
sudo /opt/seagate/s3/starts3.sh <Number of instances>
```

Example: To run three s3 service instances
```sh
sudo /opt/seagate/s3/starts3.sh 3
```

## How to run s3 server via systemctl
```sh
sudo systemctl start s3server
```

## How to stop all running s3 services ( This will stop even all multiple instances of s3 service, if its running )
```sh
sudo /opt/seagate/s3/stops3.sh
```

## How to stop s3 server service via systemctl
```sh
sudo systemctl stop s3server
```

## How to see status of s3 service
```sh
sudo /opt/seagate/s3/statuss3.sh
```


## Steps to create Java key store and Certificate.
```sh
keytool -genkey -keyalg RSA -alias s3auth -keystore s3_auth.jks -storepass seagate -validity 360 -keysize 2048
```
What is your first and last name?
   [Unknown]: signin.seagate.com
What is the name of your organization unit?
   [Unknown]: s3
What is the name of your organization?
   [Unknown]: seagate
What is the name of your City or Locality?
   [Unknown]: Pune
What is the name of your State or Province?
   [Unknown]: MH
What is the two-letter country code for this unit?
   [Unknown]: IN
is CN=signin.seagate.com, OU=s3, O=seagate, L=Pune, ST=MH, C=IN correct?
   [no]: yes

Enter key password for <s3auth>
	(RETURN if same as keystore password):

## Steps to generate crt from Key store
```sh
keytool -importkeystore -srckeystore s3_auth.jks -destkeystore s3_auth.p12 -srcstoretype jks -deststoretype pkcs12
```

```sh
openssl pkcs12 -in s3_auth.jks.p12 -out s3_auth.jks.pem
```

```sh
openssl x509 -in seagates3.pem -out seagates3.crt
```

## How to generate S3 server RPM
Following dependencies are required to build rpms.
```sh
yum install ruby
yum install ruby-devel
gem install bundler
gem install fpm
```
To generate s3server RPMs, follow build & install steps from above section
`Steps for Release environment`. After that execute below command:
```sh
./makerpm
# Above should generate a rpm file in current folder
```

### How to get unit test code coverage for Authserver?
```sh
cd auth

mvn clean package -Djacoco.skip=false
```
### How to get system test code coverage for Authserver?
```sh
cd auth

mvn clean package

# Run authserver with jacoco agent
java -javaagent:/path/to/jacocoagent.jar=destfile=target/coverage-reports/jacoco.exec,append=false \
-jar /path/to/AuthServer-1.0-0.jar

# Example:
# Note: This example uses the jacocoagent jar file downloaded by maven in local repo
# Maven local repo path: ${HOME}/.m2/repository
java \
-javaagent:${HOME}/.m2/repository/org/jacoco/org.jacoco.agent/0.7.7.201606060606/\
org.jacoco.agent-0.7.7.201606060606-runtime.jar=destfile=target/coverage-reports/\
jacoco.exec,append=false -jar /root/mero/fe/s3/auth/target/AuthServer-1.0-0.jar

# Activate python test virtualenv
source mero_st/bin/activate

# Run system test
python auth_spec.py

# Stop auth server [ Ctrl + c ].

# Generate coverage report site from coverage data file generated in above step
$ mvn jacoco:report -Djacoco.skip=false
```

### How to setup ssl
```sh
$ cd ssl

# This script will generate ssl certificates and display information on ssl setup.
$ ./setup.sh

```

## StatsD configuration
By default, Stats feature is disabled. To enable the same, edit the S3 server
config file /opt/seagate/s3/conf/s3config.yaml & set the S3_ENABLE_STATS to true.
After above config change, s3server needs to be restarted.

Before starting StatsD daemon, select backends to be used. StatsD can send data
to multiple backends. By default, through config file, StatsD is configured to
send data only to console backend. To enable sending data to Graphite backend,
after s3server installation, edit the file /opt/seagate/s3/statsd/s3statsd-config.js
Un-comment lines having Graphite variables (graphiteHost & graphitePort) and set their
values. Also add graphite to the backends variable as shown in the comment in the
s3statsd-config.js file.

Once above config is done, run StatsD daemon as below
```sh
sudo systemctl restart s3statsd
```

## Viewing StatsD data
a) Console backend
StatsD data can be viewed from /var/log/messages file. Alternatively the data
can also be viewed by telnetting to the management port 8126. The port number
is configurable in the s3statsd-config.js file. Common commands are:
help, stats, counters, timers, gauges, delcounters, deltimers, delgauges,
health, config, quit etc.

eg:
```sh
$ echo "help" | nc 127.0.0.1 8126
  Commands: stats, counters, timers, gauges, delcounters, deltimers, delgauges,
            health, config, quit

$ echo "stats" | nc 127.0.0.1 8126
  uptime: 30
  messages.last_msg_seen: 30
  messages.bad_lines_seen: 0
  console.lastFlush: 1481173145
  console.lastException: 1481173145
  END

$ echo "counters" | nc 127.0.0.1 8126
  { 'statsd.bad_lines_seen': 0,
    'statsd.packets_received': 1,
    'statsd.metrics_received': 1,
    total_request_count: 1 }
  END
```

b) Graphite backend
Open a browser on the machine hosting Graphite. Type 127.0.0.1 as the URL.
Graphite will show a dashboard, select a metric name. Graphite will display
the corresponding stats data in the form of graphs.

## Graphite installation
For the sake of simplicity, install Graphite on a VM running Ubuntu 14.04 LTS.

```sh
# Download synthesize which in turn will install Graphite
cd ~/Downloads/
wget https://github.com/obfuscurity/synthesize/archive/v2.4.0.tar.gz
tar -xvzf v2.4.0.tar.gz
cd synthesize-2.4.0/

# Edit install file: comment out lines concerning installation of collectd and
statsite.

# Run the install script
sudo ./install

# Refer to https://github.com/etsy/statsd/blob/master/docs/graphite.md and
  edit files:
    /opt/graphite/conf/storage-schemas.conf and
    /opt/graphite/conf/storage-aggregation.conf

# Restart Graphite
sudo service carbon-cache restart
```
