# Testception
Submission material for TREL 2026 paper "Testception: Recursive LLM Agents for Scalable Black-Box Web Application Testing"

This repository contains the tool implementing the approach described in an TREL26 submission, together with the subjects used in the evaluation. Testception has been tested in Ubuntu (24.04 LTS).

## 1. Automatic Setup (todo)
A virtual machine running Ubuntu 24.04 LTS is available for download [here](https://drive.switch.ch/index.php/s/w307bjzHz3tmwXm/download) . The virtual machine contains this repository and all the dependencies needed to run DANTE on the test suite subjects. 

The virtual machine was created with Qemu/KVM and was exported in the `.QCOW2` format, a platform-independent distribution format for virtual machines. It can be imported by any virtualization software although it was tested only on Qemu/KVM. Instructions on how to import an `.QCOW2` format virtual machine in Qemu/KVM is listed below:

- Qemu/KVM: https://help.quali.com/install-configure/linux-virtual-appliance/installation-procedure/create-es-from-qcow2

The minimum amount of RAM to assign to the virtual machine is `4GB`.

Login credentials:
- username: `testception`
- password: `testception`

If the automatic setup worked, you can skip to [the run experiments section](#2-run-the-experiments-crawling---after-the-setup). Otherwise procede to the [manual setup section](#11-manual-setup).

#### 1.1 Manual Setup
- Install java 1.8 ([instructions for Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-22-04));

- Install [Maven 3.6.0](https://archive.apache.org/dist/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.zip);

- Install `crawljax` locally by going to the [directory](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/crawljax) on the terminal and typing `mvn install -DskipTests`;

- Create the directory `~/.m2/repository/com` and move inside it the content of the [atlassian-custom-library](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/atlassian-custom-library) directory. This is because the `sourcemap` library version (1.7.7) used in this project is no longer supported;

- Rename the [app.example.properties](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/dante/src/main/resources/app.example.properties) file as `app.properties` and the [log4j.example.properties](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/dante/src/main/resources/log4j.example.properties) file as `log4j.properties`.



## 2. Run the experiments (Crawling - after the setup)
The script to run the crawling is [run-crawling.sh](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/dante/run-crawling.sh). 

The first argument is the `application_name`. The available values are:

- `phoenix|dimeshift|splittypie|retroboard|petclinic|ecommerce`

The second argument is the `headless` flag which determines if the browser starts headless or with the GUI. The available values are:

- `true|false`

The third argument is a number, `crawling_max_runtime` which determines a timeout for the crawler.

We are going to choose the `ecommerce` application to show how the tool works, since the single steps that come later are much faster to execute than with the subject systems we used in the paper. The results in the paper can be replicated but it simply takes longer:

- the output of the crawler applied to the subject systems will be available soon;

- the coverage reports for the subject systems used in the paper will be available soon;

The following commands in this README assume you are in the `~/workspace/TREL26-Submission-Material-Testception/dante` folder, assuming that `~` indicates the path to the home directory in your system:

- `./run-crawling.sh ecommerce false 5` (the crawler terminates the exploration in ~3 min)

After crawling the folder `dante/applications/ecommerce/localhost/crawl-with-inputs` is created, which contains the results of the crawling. In the folder `dante/applications/ecommerce` there is a file called `selenium-actions-ecommerce-fired.txt` which lists the test cases created by the crawler while it was exploring the application. The crawling generates around 18 tests.

## 3. Run the Testception 
The script to run the Testception is [mentor_orchestrator.py](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/Testception/rlm_project/mentor_orchestrator.py). 

Only argument is `application_name`. 

The command generates the java project with the test suite. 

### Steps
First, navigate to the project directory:

- `cd Testception/rlm_project` 

Then create and activate a virtual environment:
 
- `python3 -m venv rlm_env` 
- `source rlm_env/bin/activate` 

Since Testception is an LLM-based test generation framework, it requires an OpenRouter API key to access the GPT-4o-mini model during scenario generation. The model is used to analyze Crawljax-generated DOM states and produce test scenarios automatically.

Export your OpenRouter API key:

- `export OPENROUTER_API_KEY="<your_api_key>"` 

Finally, run Testception with the target application name:

- `python3 mentor_orchestrator.py ecommerce` 


## 4. Coverage 
In this step, the default DANTE test suite generation is disabled, and the experiments proceed with the custom test suites generated by Testception.

Instead of using DANTE’s original generated scenarios from files, we replace the generated Java test classes with the Testception-generated test suites and continue the pipeline with those files.. The script used in this step is [generate-java-project-from-crawling.sh](https://https://github.com/iOTMecit/TREL26-Submission-Material-Testception/master/dante/generate-java-project-from-crawling.sh). 

First, navigate back to the dante project directory:

- `cd workspace/TREL26-Submission-Material-Testception/dante` 

The first argument is `application_name` and the second argument is `headless`. Following with the `ecommerce` example the command to run is:

- `./generate-java-project-from-crawling.sh ecommerce false` 

At this stage, DANTE’s original test scenario generation is bypassed, and the pipeline continues with the custom Java test suites placed by Testception. The command generates the Java project structure, runs the project with the injected Testception test suites, and collects coverage based on those custom tests.

## 5. Comparison 
Here we compare Testception with DANTE,AWET, Crawljax and Atusa in terms of JS code coverage. The script to run the test minimization is [run-tests-and-measure-coverage.sh](https://github.com/matteobiagiola/TREL26-Submission-Material-Testception/master/dante/run-tests-and-measure-coverage.sh). The first argument is the `application_name`, the second argument is the `headless` flag and the third argument is the `technique` whose values are `dante|crawljax|atusa|testception`.

To run the comparison we execute the following commands:

- `./run-tests-and-measure-coverage.sh ecommerce true testception`

- `./run-tests-and-measure-coverage.sh ecommerce true dante coverage_driven`

- `./run-tests-and-measure-coverage.sh ecommerce true crawljax`

- `./run-tests-and-measure-coverage.sh ecommerce true atusa`

The log files `logs_RunTests_[technique]_ecommerce.txt` in the `Desktop` folder report the tests that passed as well as the tests that broke. The log files `logs_MeasureCoverageOfTests_[technique]_ecommerce.txt` in the `Desktop` folder report the code coverage achieved by the non-broken tests.

## Attribution

Testception is built on top of the DANTE execution and coverage infrastructure and reuses parts of the original DANTE project.

The original DANTE project is available at: https://github.com/matteobiagiola/TREL26-Submission-Material-Testception

This repository includes modifications and extensions developed for Testception, including LLM-based scenario generation, custom orchestration, and injected test-suite generation.
