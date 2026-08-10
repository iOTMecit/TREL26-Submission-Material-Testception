package com.dante.subjects.config;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.OnUrlFirstLoadPlugin;
import com.crawljax.core.configuration.Form;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.forms.FormInput;
import com.dante.subjects.ApplicationNames;
import com.dante.subjects.Config;
import com.dante.suitegenerator.SuiteGeneratorConfig;
import com.dante.tedd.extraction.DependencyGraphExtractionConfig;
import com.dante.utils.Properties;
import java.net.URI;
import org.openqa.selenium.WebElement;

public class PetclinicConfig extends Config {

    private static final String url = "http://localhost:3000";
    private static final int waitTimeAfterReload = 3000;

    private static void waitForAngular() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                    "Petclinic prerequisite setup was interrupted",
                    exception);
        }
    }

    private static WebElement requiredElement(
            EmbeddedBrowser browser,
            String xpath,
            String description) {

        Identification identification =
                new Identification(
                        Identification.How.xpath,
                        xpath);

        WebElement element = browser.getWebElement(identification);

        if (element == null) {
            throw new IllegalStateException(
                    "Petclinic prerequisite element was not found: "
                            + description
                            + " | "
                            + xpath);
        }

        return element;
    }

    private static void seedPetType(
            EmbeddedBrowser browser,
            String petTypeName) {

        browser.goToUrl(
                URI.create(
                        url + "/petclinic/pettypes"));
        waitForAngular();

        requiredElement(
                browser,
                "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                        + "/DIV[1]/DIV[1]/DIV[1]/BUTTON[2]",
                "Pet Types Add button")
                .click();
        waitForAngular();

        WebElement nameInput =
                requiredElement(
                        browser,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/APP-PETTYPE-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/DIV[1]/INPUT[1]",
                        "Pet Type name input");

        nameInput.clear();
        nameInput.sendKeys(petTypeName);

        requiredElement(
                browser,
                "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                        + "/DIV[1]/DIV[1]/DIV[1]/APP-PETTYPE-ADD[1]"
                        + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                        + "/BUTTON[1]",
                "Pet Type Save button")
                .click();
        waitForAngular();
    }

    private static void seedSpecialty(
            EmbeddedBrowser browser,
            String specialtyName) {

        browser.goToUrl(
                URI.create(
                        url + "/petclinic/specialties"));
        waitForAngular();

        requiredElement(
                browser,
                "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                        + "/DIV[1]/DIV[1]/DIV[1]/BUTTON[2]",
                "Specialties Add button")
                .click();
        waitForAngular();

        WebElement nameInput =
                requiredElement(
                        browser,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/APP-SPECIALTY-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/DIV[1]/INPUT[1]",
                        "Specialty name input");

        nameInput.clear();
        nameInput.sendKeys(specialtyName);

        requiredElement(
                browser,
                "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                        + "/DIV[1]/DIV[1]/DIV[1]/APP-SPECIALTY-ADD[1]"
                        + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                        + "/BUTTON[1]",
                "Specialty Save button")
                .click();
        waitForAngular();
    }

    private static void seedReferenceData(
            CrawlerContext context,
            String petTypeName,
            String specialtyName) {

        EmbeddedBrowser browser = context.getBrowser();

        System.out.println(
                "PETCLINIC_PREREQUISITE_START"
                        + " | petType="
                        + petTypeName
                        + " | specialty="
                        + specialtyName);

        try {
            seedPetType(
                    browser,
                    petTypeName);
            seedSpecialty(
                    browser,
                    specialtyName);

            /*
             * Return to the normal landing page before Crawljax creates the
             * index state. Pet Types and Specialties now exist for Pet and Vet
             * forms, while their CRUD pages remain available to the crawler.
             */
            browser.goToUrl(
                    URI.create(
                            url + "/petclinic/welcome"));
            waitForAngular();

            System.out.println(
                    "PETCLINIC_PREREQUISITE_DONE");
        } catch (RuntimeException exception) {
            System.out.println(
                    "PETCLINIC_PREREQUISITE_FAILED: "
                            + exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Could not seed Petclinic prerequisite data",
                    exception);
        }
    }

    public PetclinicConfig() {
        super(ApplicationNames.Name.PETCLINIC.getName());
    }

    @Override
    public CrawljaxConfiguration.CrawljaxConfigurationBuilder getCrawljaxConfig() {

        System.out.println(
                "PETCLINIC_CONFIG_PROFILE=RECORDED_FULL_FLOW_V4_VISIT_PRIORITY");

        final String runSuffix =
                String.valueOf(
                        System.currentTimeMillis() % 100000L);

        final String seedPetTypeName =
                "BootstrapType" + runSuffix;
        final String seedSpecialtyName =
                "BootstrapSpecialty" + runSuffix;

        /*
         * The real crawl graph starts from the normal Petclinic welcome page.
         * OnUrlFirstLoadPlugin temporarily creates one Pet Type and one
         * Specialty through the UI, then returns to /petclinic/welcome before
         * Crawljax records the index state. This keeps dependent Pet/Vet forms
         * usable without turning Pet Types into an isolated bootstrap root.
         */
        final String initialUrl =
                url + "/petclinic/welcome";

        CrawljaxConfiguration.CrawljaxConfigurationBuilder builder =
                this.crawljaxCommonConfig(
                        initialUrl,
                        Properties.MAX_RUNTIME,
                        1000,
                        waitTimeAfterReload);

        builder.addPlugin(
                new OnUrlFirstLoadPlugin() {
                    @Override
                    public void onUrlFirstLoad(
                            CrawlerContext context) {

                        seedReferenceData(
                                context,
                                seedPetTypeName,
                                seedSpecialtyName);
                    }

                    @Override
                    public String toString() {
                        return "Petclinic recorded full-flow prerequisite plugin";
                    }
                });

        /*
         * FIRED is retained because the Owners and Veterinarians dropdowns
         * must be reopened from different application states. Loops are
         * controlled with explicit business selectors instead of registering
         * every A and BUTTON element.
         */
        builder.setConsiderCandidateElementsOnce(
                CrawljaxConfiguration.CandidateElementsMode.fired);

        // ==============================================================
        // Main navigation
        // ==============================================================
        /*
         * The first-load plugin guarantees at least one Pet Type and one
         * Specialty. The normal crawler still revisits both CRUD pages, so
         * Add/Save/Edit/Update/Delete transitions can appear in result.json.
         */

        builder.crawlRules().click("A")
                .withText("OWNERS");
        builder.crawlRules().click("A")
                .withAttribute("href", "/petclinic/owners");
        builder.crawlRules().click("A")
                .withAttribute("href", "/petclinic/owners/add");

        builder.crawlRules().click("A")
                .withText("VETERINARIANS");
        builder.crawlRules().click("A")
                .withAttribute("href", "/petclinic/vets");
        builder.crawlRules().click("A")
                .withAttribute("href", "/petclinic/vets/add");

        builder.crawlRules().click("A")
                .withAttribute("href", "/petclinic/pettypes");
        builder.crawlRules().click("A")
                .withAttribute("href", "/petclinic/specialties");

        /*
         * Do not spend crawl time repeatedly returning to the landing page.
         * Crawljax backtracking already restores predecessor states.
         */
        builder.crawlRules().dontClick("A")
                .withText("HOME");
        builder.crawlRules().dontClick("BUTTON")
                .withText("Home");

        // ==============================================================
        // Owner flow
        // ==============================================================

        /*
         * Open the most recently listed owner. After Add Owner this points to
         * the newly created deterministic test owner instead of traversing
         * every seeded owner and multiplying equivalent branches.
         */
        builder.crawlRules().click("A")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-OWNER-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]"
                                + "/TR[last()]/TD[1]");

        builder.crawlRules().click("BUTTON")
                .withText("Add Owner");
        builder.crawlRules().click("BUTTON")
                .withText("Edit Owner");
        builder.crawlRules().click("BUTTON")
                .withText("Update Owner");
        builder.crawlRules().click("BUTTON")
                .withText("Add New Pet");

        // ==============================================================
        // Pet and visit flow
        // ==============================================================

        builder.crawlRules().click("BUTTON")
                .withText("Save Pet");

        /*
         * Visit flow is registered before the Edit Pet branch so the newly
         * created pet can reach Add Visit before crawl time is consumed by
         * edit/delete alternatives.
         *
         * The owner-detail opener and the visit-form submit share the visible
         * text "Add Visit", so use their component-specific XPaths.
         */
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-OWNER-DETAIL[1]"
                                + "/DIV[1]/DIV[1]/TABLE[2]/TBODY[1]/TR[1]"
                                + "/APP-PET-LIST[1]/TABLE[1]/TBODY[1]/TR[1]"
                                + "/TD[1]/DL[1]/BUTTON[3]");

        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VISIT-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/BUTTON[2]");

        builder.crawlRules().click("BUTTON")
                .withText("Edit Visit");
        builder.crawlRules().click("BUTTON")
                .withText("Update Visit");
        builder.crawlRules().click("BUTTON")
                .withText("Delete Visit");

        /*
         * Keep the Pet edit/update branch, but temporarily disable Delete Pet.
         * In the previous crawl state35 exposed Edit Pet, Delete Pet and
         * Add Visit together; Crawljax consumed Edit/Delete and reached
         * MAX_TIME before traversing Add Visit.
         */
        builder.crawlRules().click("BUTTON")
                .withText("Edit Pet");
        builder.crawlRules().click("BUTTON")
                .withText("Update Pet");
        builder.crawlRules().dontClick("BUTTON")
                .withText("Delete Pet");

        // ==============================================================
        // Veterinarian flow
        // ==============================================================

        builder.crawlRules().click("BUTTON")
                .withText("Add Vet");
        builder.crawlRules().click("BUTTON")
                .withText("Save Vet");

        /*
         * Explore Edit Vet and Delete Vet only on the last table row. This
         * covers both branches while avoiding one branch per seeded vet.
         */
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-LIST[1]"
                                + "/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]"
                                + "/TR[last()]/TD[3]");

        /*
         * The edit-vet specialty control is Angular Material rather than a
         * native SELECT. Register its semantic elements and the backdrop used
         * to close the multi-select before Save Vet.
         */
        builder.crawlRules().click("MAT-SELECT");
        builder.crawlRules().click("MAT-OPTION");
        builder.crawlRules().click("DIV")
                .withAttribute(
                        "class",
                        "cdk-overlay-backdrop cdk-overlay-transparent-backdrop cdk-overlay-backdrop-showing");

        // ==============================================================
        // Pet Types and Specialties
        // ==============================================================

        /*
         * Pet Type: Add -> name -> Save, then Edit/Update/Delete.
         * These exact component selectors follow the newly recorded flow and
         * avoid confusing the Pet Type and Specialty buttons that share the
         * same visible labels.
         */
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/BUTTON[2]");
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/APP-PETTYPE-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/BUTTON[1]");
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/BUTTON[1]");

        /*
         * Specialty: Add -> name -> Save, then Edit/Update/Delete.
         */
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/BUTTON[2]");
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/APP-SPECIALTY-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/BUTTON[1]");
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/BUTTON[1]");

        /*
         * Work on the most recently created row. Delete is enabled only when
         * the table contains more than one row, so the prerequisite seed row
         * always remains available for Pet and Vet forms.
         */
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                                + "/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]"
                                + "/TR[last()]/TD[2]/BUTTON[1]");
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                                + "/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]"
                                + "[count(TR) > 1]/TR[last()]/TD[2]/BUTTON[2]");

        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                                + "/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]"
                                + "/TR[last()]/TD[2]/BUTTON[1]");
        builder.crawlRules().click("BUTTON")
                .underXPath(
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                                + "/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]"
                                + "[count(TR) > 1]/TR[last()]/TD[2]/BUTTON[2]");

        // ==============================================================
        // Low-value / loop-prone controls
        // ==============================================================

        builder.crawlRules().dontClick("BUTTON")
                .withText("Back");
        builder.crawlRules().dontClick("BUTTON")
                .withText("< Back");
        builder.crawlRules().dontClick("BUTTON")
                .withText("Cancel");

        /*
         * Dates are written directly by InputSpecification. Avoid Angular
         * Material calendar overlays, whose day/month/year cells generate a
         * large number of visually similar states.
         */
        builder.crawlRules().dontClick("BUTTON")
                .withAttribute("aria-label", "Open calendar");
        builder.crawlRules().dontClick("BUTTON")
                .withAttribute("class", "mat-icon-button");

        InputSpecification inputSpecification = new InputSpecification();

        /*
         * Values are unique per crawl so Add Owner, Pet Type and Specialty
         * remain repeatable even when the application's database is retained
         * between runs.
         */
        String telephoneValue =
                String.format(
                        "555%07d",
                        System.currentTimeMillis() % 10000000L);

        // ==============================================================
        // Add Owner
        // ==============================================================

        Form newOwnerForm = new Form();

        FormInput ownerFirstName = newOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "firstName"));
        ownerFirstName.inputValues("RLMOwner");

        FormInput ownerLastName = newOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "lastName"));
        ownerLastName.inputValues("Test" + runSuffix);

        FormInput ownerAddress = newOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "address"));
        ownerAddress.inputValues("Test Automation Street");

        FormInput ownerCity = newOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "city"));
        ownerCity.inputValues("Izmir");

        FormInput ownerTelephone = newOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "telephone"));
        ownerTelephone.inputValues(telephoneValue);

        inputSpecification.setValuesInForm(newOwnerForm)
                .beforeClickElement("BUTTON")
                .withText("Add Owner");

        // ==============================================================
        // Edit Owner
        // ==============================================================

        Form editOwnerForm = new Form();

        FormInput editedOwnerFirstName = editOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "firstName"));
        editedOwnerFirstName.inputValues("RLMOwnerUpdated");

        FormInput editedOwnerLastName = editOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "lastName"));
        editedOwnerLastName.inputValues("Updated" + runSuffix);

        FormInput editedOwnerAddress = editOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "address"));
        editedOwnerAddress.inputValues("Updated Automation Street");

        FormInput editedOwnerCity = editOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "city"));
        editedOwnerCity.inputValues("Manisa");

        FormInput editedOwnerTelephone = editOwnerForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "telephone"));
        editedOwnerTelephone.inputValues(telephoneValue);

        inputSpecification.setValuesInForm(editOwnerForm)
                .beforeClickElement("BUTTON")
                .withText("Update Owner");

        // ==============================================================
        // Add / Edit Pet
        // ==============================================================

        Form addPetForm = new Form();

        FormInput newPetName = addPetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "name"));
        newPetName.inputValues("Pet" + runSuffix);

        FormInput newPetBirthDate = addPetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.name,
                        "birthDate"));
        newPetBirthDate.inputValues("2026/07/01");

        FormInput newPetType = addPetForm.inputField(
                FormInput.InputType.SELECT,
                new Identification(
                        Identification.How.id,
                        "type"));
        newPetType.inputValues(seedPetTypeName);

        inputSpecification.setValuesInForm(addPetForm)
                .beforeClickElement("BUTTON")
                .withText("Save Pet");

        Form editPetForm = new Form();

        FormInput editedPetName = editPetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "name"));
        editedPetName.inputValues("PetUpdated" + runSuffix);

        FormInput editedPetBirthDate = editPetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.name,
                        "birthDate"));
        editedPetBirthDate.inputValues("2026/04/08");

        FormInput editedPetType = editPetForm.inputField(
                FormInput.InputType.SELECT,
                new Identification(
                        Identification.How.id,
                        "type"));
        editedPetType.inputValues(seedPetTypeName);

        inputSpecification.setValuesInForm(editPetForm)
                .beforeClickElement("BUTTON")
                .withText("Update Pet");

        // ==============================================================
        // Add / Edit Visit
        // ==============================================================

        Form addVisitForm = new Form();

        FormInput newVisitDate = addVisitForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VISIT-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]"
                                + "/DIV[1]/INPUT[1]"));
        newVisitDate.inputValues("2026/07/24");

        FormInput newVisitDescription = addVisitForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "description"));
        newVisitDescription.inputValues("RLM general examination");

        inputSpecification.setValuesInForm(addVisitForm)
                .beforeClickElement("BUTTON")
                .withText("Add Visit");

        Form editVisitForm = new Form();

        FormInput editedVisitDate = editVisitForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VISIT-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]"
                                + "/DIV[1]/INPUT[1]"));
        editedVisitDate.inputValues("2026/07/10");

        FormInput editedVisitDescription = editVisitForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.id,
                        "description"));
        editedVisitDescription.inputValues(
                "RLM updated examination");

        inputSpecification.setValuesInForm(editVisitForm)
                .beforeClickElement("BUTTON")
                .withText("Update Visit");

        // ==============================================================
        // Add / Edit Pet Type
        // ==============================================================

        Form addPetTypeForm = new Form();

        FormInput newPetTypeName = addPetTypeForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/APP-PETTYPE-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/DIV[1]/INPUT[1]"));
        newPetTypeName.inputValues(
                "RLMType" + runSuffix,
                "RLMTypeAlt" + runSuffix);

        inputSpecification.setValuesInForm(addPetTypeForm)
                .beforeClickElement("BUTTON")
                .withText("Save");

        Form editPetTypeForm = new Form();

        FormInput editedPetTypeName = editPetTypeForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-PETTYPE-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/DIV[1]/INPUT[1]"));
        editedPetTypeName.inputValues(
                "RLMTypeUpdated" + runSuffix);

        inputSpecification.setValuesInForm(editPetTypeForm)
                .beforeClickElement("BUTTON")
                .withText("Update");

        // ==============================================================
        // Add / Edit Specialty
        // ==============================================================

        Form addSpecialtyForm = new Form();

        FormInput newSpecialtyName = addSpecialtyForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]"
                                + "/DIV[1]/DIV[1]/DIV[1]/APP-SPECIALTY-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/DIV[1]/INPUT[1]"));
        newSpecialtyName.inputValues(
                "RLMSpecialty" + runSuffix,
                "RLMSpecialtyAlt" + runSuffix);

        inputSpecification.setValuesInForm(addSpecialtyForm)
                .beforeClickElement("BUTTON")
                .withText("Save");

        Form editSpecialtyForm = new Form();

        FormInput editedSpecialtyName = editSpecialtyForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]"
                                + "/DIV[1]/INPUT[1]"));
        editedSpecialtyName.inputValues(
                "RLMSpecialtyUpdated" + runSuffix);

        inputSpecification.setValuesInForm(editSpecialtyForm)
                .beforeClickElement("BUTTON")
                .withText("Update");

        // ==============================================================
        // Add / Edit Veterinarian
        // ==============================================================

        Form addVetForm = new Form();

        FormInput newVetFirstName = addVetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]"
                                + "/DIV[1]/INPUT[1]"));
        newVetFirstName.inputValues("RLMVet");

        FormInput newVetLastName = addVetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-ADD[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[3]"
                                + "/DIV[1]/INPUT[1]"));
        newVetLastName.inputValues("Test" + runSuffix);

        /*
         * The recorded Add Vet page exposes a native select with id
         * "specialties". The prerequisite plugin guarantees that this option
         * exists before the Veterinarian branch is explored.
         */
        FormInput newVetSpecialty = addVetForm.inputField(
                FormInput.InputType.SELECT,
                new Identification(
                        Identification.How.id,
                        "specialties"));
        newVetSpecialty.inputValues(seedSpecialtyName);

        inputSpecification.setValuesInForm(addVetForm)
                .beforeClickElement("BUTTON")
                .withText("Save Vet");

        Form editVetForm = new Form();

        FormInput editedVetFirstName = editVetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[2]"
                                + "/DIV[1]/INPUT[1]"));
        editedVetFirstName.inputValues("RLMVetUpdated");

        FormInput editedVetLastName = editVetForm.inputField(
                FormInput.InputType.TEXT,
                new Identification(
                        Identification.How.xpath,
                        "/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-EDIT[1]"
                                + "/DIV[1]/DIV[1]/FORM[1]/DIV[3]"
                                + "/DIV[1]/INPUT[1]"));
        editedVetLastName.inputValues("Updated" + runSuffix);

        inputSpecification.setValuesInForm(editVetForm)
                .beforeClickElement("BUTTON")
                .withText("Save Vet");

        builder.crawlRules().setInputSpec(inputSpecification);

        /*
         * Prevent the same form/value combination from being resubmitted
         * repeatedly while the crawler remains in an equivalent Angular state.
         */
        builder.setHandleSameFormInputsOncePerState(true);

        return builder;
    }

    @Override
    public SuiteGeneratorConfig getSuiteGeneratorConfig() {

        SuiteGeneratorConfig suiteGeneratorConfig = this.testSuiteGeneratorCommonConfig(url, waitTimeAfterReload);

        suiteGeneratorConfig.setSourcemapURL("http://localhost:3000/main.js.map");
        suiteGeneratorConfig.setSrcCodeFolder("src");
        suiteGeneratorConfig.setScriptNameToInclude("main");
        suiteGeneratorConfig.setFiredElementStrategy(true);

        return suiteGeneratorConfig;
    }

    @Override
    public DependencyGraphExtractionConfig getDependencyGraphExtractionConfig() {
        String dependencyGraphOptimized = "1 00 110 0100 10000 000001 0001000 11010000 100101000 0011000000 00000000000 010001000010 0000000000000 00000100000000 101000011010010 0100011001000000 00110000000000000 110001101000110000 0110010001100000000 10010000110000001000 001001000110000000000 0001100000000110000000 00000000011000000000000 101010001001000000000000 1100101011000000000000000 00101110000000100001000000 100100011111000000000000000 0101101100000100000000000000 00000111101011010000000000000 101000000010110000000001000000 0000000001011100000000010000000 01000011000101000000000100000000 000000000011000000000000000000000 1100110110100000110000000000000000 01101010000100100000000000000010000 100010001011000001000001100000001000 0100000010000010010000000000000000000 00000010000101000001100000000000000000 111101100110001000100010110000000000000 0111101001000100011000000000001000000000 10001000010100101000010000000000000000000 001000000000001100001001000000000000000000 0100011010011000010001001000000100000000000 00011000101100010000001010101100000000000000 110010000010111101000000000000010000000000000 1110110010001001000100001100000001000100010000 00001001111011001000110001000010010000100101000 000011100001100000000000000000000000000000000000 0010100101001010000000010000101000000000000000000 11001100000000001000001011110010100000010000000011 011000001000100010000000000100010000000000000000000 1110000001000000001000010001000001000000000000000000 10000000001100000100010000000000000000010000000001000 001001000010011011110100001100010000000000100000000001 0000011010001100001000010010010000001000000000001100010 01101000000000010100000100010000000001000001000001000000 001101100000011010010000010000011110100000010000000000000 1010010000110111000010000000010000000000100000000000000000 10100100110010111101000100101110000100100000000000000000000 011000110000101110000000001010101001001000100010000000000001 0110100000000010010000000100011001000000000001100001000000000 00000110010100000011000011111100000000000010000000010000000000 000000000100101000010000011000000000000000000000000000000000000 0110011110000011000010001000101010101000000001101000000001000000";

        DependencyGraphExtractionConfig dependencyGraphExtractionConfig = new DependencyGraphExtractionConfig();
        dependencyGraphExtractionConfig.setParetoFrontSolution(dependencyGraphOptimized);
        // window strategy
//        dependencyGraphExtractionConfig.setFixedMinimizedTestSuite("00100001100000000000000100000000001110001010100000001000000000000");
        // one by one strategy
        dependencyGraphExtractionConfig.setFixedSelectedTestSuite("11111111111111111111111111111111111110001010100000001000000000000");

        return dependencyGraphExtractionConfig;
    }
}
