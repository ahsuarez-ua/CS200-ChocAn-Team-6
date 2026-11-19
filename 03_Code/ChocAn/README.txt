CHOCAN DATA PROCESSING SYSTEM - CS 200 PROJECT 4 (TEAM 6)
==========================================================

Team 6: Jacob Wilson, Aaron Suarez, Gabriel Mckinney, Peyton Hardy
Repository: https://github.com/ahsuarez-ua/CS200-ChocAn-Team-6

A command-line Java implementation of the ChocAn system designed in
Projects 1-3. The classes follow the Project 3 class diagram; the final
diagram is in the UML folder (ChocAn_Class_Diagram.png / .pdf).


1. REQUIREMENTS
---------------
Java JDK 17 or newer (tested with JDK 21 and JDK 25). No other libraries.


2. COMPILE AND RUN FROM THE COMMAND LINE
----------------------------------------
Open a terminal in this folder (the one containing README.txt and src).

  Windows (Command Prompt or PowerShell):
      mkdir bin
      javac -d bin src\chocan\*.java
      java -cp bin chocan.Menu

  macOS / Linux:
      mkdir -p bin
      javac -d bin src/chocan/*.java
      java -cp bin chocan.Menu

The program creates two folders next to README.txt the first time it runs:
  data/     saved records (members, providers, services, service records, logins)
  reports/  every report, the EFT file, and the provider directory


3. RUN IN ECLIPSE OR VS CODE
----------------------------
Eclipse:  File > Import > General > Existing Projects into Workspace,
          select this folder, Finish. Open src/chocan/Menu.java and click Run.
VS Code:  File > Open Folder, select this folder (needs the
          "Extension Pack for Java"). Open src/chocan/Menu.java and click
          "Run" above main().

Both run from the project folder, so data/ and reports/ appear here.


4. LOGIN NUMBERS FOR TESTING
----------------------------
The first run creates sample data so every feature can be tried at once.

  Operator:   111111111
  Manager:    999999999
  Providers:  200000001  Dr. Sarah Mitchell
              200000002  Fit Life Center
              200000003  Dr. James Patel
  Members:    100000001  Ava Thompson    (valid)
              100000002  Ben Carter      (valid)
              100000003  Chloe Nguyen    (SUSPENDED)
              100000004  Daniel Brooks   (valid)
  Service codes: 598470 Dietitian $75.00, 883948 Aerobics Session $40.00,
                 112233 Internist Consult $150.00, 445566 Exercise Physiology $60.00,
                 778899 Group Therapy $35.00, 102938 Nutrition Workshop $25.00

Sample service records are dated within the past week, so reports and the
accounting procedure have data immediately.


5. MAIN MENU
------------
  1. Operator terminal   add / update / delete / list members and providers
  2. Provider terminal   validate member, bill ChocAn (service record),
                         request provider directory
  3. Manager terminal    member, provider, and summary reports for any week
  4. Scheduler           runs the main accounting procedure now (normally
                         Friday at midnight): member reports, provider reports,
                         summary report, and EFT data
  5. ACME Accounting     suspend or reinstate a member
  0. Exit

Reports are written to reports/week-ending-MM-DD-YYYY/ (member reports in
members/, provider reports in providers/). The provider directory is written
to reports/ProviderDirectory.txt.


6. DATA PERSISTENCE (EXTRA CREDIT)
----------------------------------
Every change is saved to the data folder immediately. To check it:
  1. Add a member (or bill a service), then choose 0 to exit.
  2. Start the program again: it prints "Loaded saved records".
  3. List members in the operator terminal; the new member is still there.

To reset to the sample data, delete the data folder and run again.


7. SOURCE FILES (src/chocan)
----------------------------
Boundary:  Menu (main), OperatorTerminal, ProviderTerminal, ManagerTerminal,
           Scheduler, ACMEInterface, ConsoleInput
Control:   VerificationController, MemberController, ProviderController,
           ServiceController, AccountingController, ReportController
Entity:    Member, Provider, Operator, Manager, Service, ServiceRecord,
           MemberRegistry, ProviderRegistry, OperatorRegistry, ManagerRegistry,
           ServiceCatalog, ServiceLog, ProviderDirectory, Report, ReportLine,
           MemberReport, ProviderReport, SummaryReport, PaymentFile,
           PaymentRecord, RecordFile, MemberStatus, ValidationResult
