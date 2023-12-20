package tests.authorizationpolicy.opd;

import com.healthgraph.SeleniumFramework.Util.Common.Cls_Generic_Methods;
import org.testng.annotations.Test;
import pages.authorizationPolicy.Page_InventoryPolicy;
import pages.commonElements.CommonActions;
import pages.commonElements.Page_CommonElements;
import pages.commonElements.bills.Page_Bills;
import pages.commonElements.navbar.Page_Navbar;
import pages.commonElements.patientAppointmentDetails.Page_PatientAppointmentDetails;
import pages.opd.Page_OPD;
import pages.settings.organisationSettings.general.Page_OrganisationSetup;
import pages.store.Page_PatientQueue;

public class AdvancePolicyTest extends OPD_Policy{

    Page_Navbar oPage_Navbar;
    Page_OrganisationSetup oPage_OrganisationSetup;
    Page_InventoryPolicy oPage_InventoryPolicy;
    Page_CommonElements oPage_CommonElements;
    Page_OPD oPage_OPD;
    Page_PatientAppointmentDetails oPage_PatientAppointmentDetails;
    Page_Bills oPage_Bills;
    Page_PatientQueue oPage_PatientQueue;
    String sCreateAdvancePolicyComponent = "CREATE (ADVANCE)";
    String sCancelAdvancePolicyComponent = "CANCEL (ADVANCE)";
    String sRefundAdvancePolicyComponent = "REFUND (ADVANCE)";


    @Test(description = "Validate OPD Advance create access")
    public void validatePolicy_createAdvance() {
        oPage_Navbar = new Page_Navbar(driver);
        oPage_OrganisationSetup = new Page_OrganisationSetup(driver);
        oPage_InventoryPolicy = new Page_InventoryPolicy(driver);
        oPage_CommonElements = new Page_CommonElements(driver);
        oPage_OPD = new Page_OPD(driver);
        oPage_PatientAppointmentDetails = new Page_PatientAppointmentDetails(driver);
        oPage_Bills=new Page_Bills(driver);

        try {

            setPolicy(sCreateAdvancePolicyComponent, false);
            enableOrDisableTimeSlot(false);
            adminTab = driver.getWindowHandle();

            if (iDriver == null) {
                Cls_Generic_Methods.switchToNewTab(driver.getWindowHandle());
                Cls_Generic_Methods.getURL(driver, Cls_Generic_Methods.getConfigValues("URL_" + env.toUpperCase()));
                Cls_Generic_Methods.waitForPageLoad(driver, 20);
                opdTab = driver.getWindowHandle();
            }

            switchToPolicyUser();
            createPatient();

            Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_clickBills, 10);
            Cls_Generic_Methods.clickElement(oPage_Bills.button_clickBills);
            Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_advanceReceiptBill,10);

            boolean createNewBillBtnEnabled =isButtonEnabled(oPage_Bills.button_advanceReceiptBill);
            m_assert.assertFalse(createNewBillBtnEnabled, "<font color='blue'>Validated " + sPolicyDescription + " is disabled for the user</font>");

            switchToAdmin();
            setPolicy(sCreateAdvancePolicyComponent, true);
            Cls_Generic_Methods.customWait();

            switchToPolicyUser();
            Cls_Generic_Methods.driverRefresh();
            Cls_Generic_Methods.waitForPageLoad(driver, 20);
            selectPatientFromAllTab();

            Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_clickBills, 10);
            Cls_Generic_Methods.clickElement(oPage_Bills.button_clickBills);
            Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_advanceReceiptBill,10);

            createNewBillBtnEnabled = isButtonEnabled(oPage_Bills.button_advanceReceiptBill);
            m_assert.assertTrue(createNewBillBtnEnabled, "<font color='blue'>Validated " + sPolicyDescription + " is enabled for the user</font>");

        } catch (Exception e) {
            e.printStackTrace();
            m_assert.assertFatal("Unable to validate " + sPolicyDescription + " ->" + e);
        }

    }

    @Test(description = "Validate OPD Advance cancel access")
    public void validatePolicy_cancelAdvance() {
        oPage_Navbar = new Page_Navbar(driver);
        oPage_OrganisationSetup = new Page_OrganisationSetup(driver);
        oPage_InventoryPolicy = new Page_InventoryPolicy(driver);
        oPage_CommonElements = new Page_CommonElements(driver);
        oPage_OPD = new Page_OPD(driver);
        oPage_PatientAppointmentDetails = new Page_PatientAppointmentDetails(driver);
        oPage_Bills=new Page_Bills(driver);
        oPage_PatientQueue=new Page_PatientQueue(driver);

        try {

            setPolicy(sCancelAdvancePolicyComponent, false);
            enableOrDisableTimeSlot(false);
            adminTab = driver.getWindowHandle();

            if (iDriver == null) {
                Cls_Generic_Methods.switchToNewTab(driver.getWindowHandle());
                Cls_Generic_Methods.getURL(driver, Cls_Generic_Methods.getConfigValues("URL_" + env.toUpperCase()));
                Cls_Generic_Methods.waitForPageLoad(driver, 20);
                opdTab = driver.getWindowHandle();
            }

            switchToPolicyUser();
            createPatient();

            Cls_Generic_Methods.waitForElementsToBeDisplayed(oPage_Bills.list_previousAdvanceReceipts,10);

            if(oPage_Bills.list_previousAdvanceReceipts.size()==0){
                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_clickBills, 10);
                Cls_Generic_Methods.clickElement(oPage_Bills.button_clickBills);
                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_advanceReceiptBill,10);
                Cls_Generic_Methods.clickElement(oPage_Bills.button_advanceReceiptBill);

                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_advanceReceiptTemplate, 5);
                Cls_Generic_Methods.sendKeysIntoElement(oPage_PatientQueue.input_reasonAdvance, "Test");
                Cls_Generic_Methods.selectElementByVisibleText(oPage_PatientQueue.select_mop, "Cash");
                Cls_Generic_Methods.sendKeysIntoElement(oPage_PatientQueue.input_amountAdvance, "100");
                Cls_Generic_Methods.customWait();
                Cls_Generic_Methods.clickElement(oPage_PatientQueue.button_saveAdvance);
                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_closeAdvance,10);
                Cls_Generic_Methods.clickElement(oPage_PatientQueue.button_closeAdvance);
                Cls_Generic_Methods.customWait();
            }

            Cls_Generic_Methods.clickElement(oPage_Bills.list_previousAdvanceReceipts.get(0));

            boolean advanceCancellationBtnEnabled =Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_cancellationAdvance,10);
            m_assert.assertFalse(advanceCancellationBtnEnabled, "<font color='blue'>Validated " + sPolicyDescription + " is disabled for the user</font>");

            switchToAdmin();
            setPolicy(sCancelAdvancePolicyComponent, true);
            Cls_Generic_Methods.customWait();

            switchToPolicyUser();
            Cls_Generic_Methods.driverRefresh();
            Cls_Generic_Methods.waitForPageLoad(driver, 20);
            selectPatientFromAllTab();

            Cls_Generic_Methods.waitForElementsToBeDisplayed(oPage_Bills.list_previousAdvanceReceipts,10);
            Cls_Generic_Methods.clickElement(oPage_Bills.list_previousAdvanceReceipts.get(0));

            advanceCancellationBtnEnabled =Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_cancellationAdvance,10);
            m_assert.assertTrue(advanceCancellationBtnEnabled, "<font color='blue'>Validated " + sPolicyDescription + " is enabled for the user</font>");

        } catch (Exception e) {
            e.printStackTrace();
            m_assert.assertFatal("Unable to validate " + sPolicyDescription + " ->" + e);
        }

    }

    @Test(description = "Validate OPD Advance refund access")
    public void validatePolicy_refundAdvance() {
        oPage_Navbar = new Page_Navbar(driver);
        oPage_OrganisationSetup = new Page_OrganisationSetup(driver);
        oPage_InventoryPolicy = new Page_InventoryPolicy(driver);
        oPage_CommonElements = new Page_CommonElements(driver);
        oPage_OPD = new Page_OPD(driver);
        oPage_PatientAppointmentDetails = new Page_PatientAppointmentDetails(driver);
        oPage_Bills=new Page_Bills(driver);
        oPage_PatientQueue=new Page_PatientQueue(driver);

        try {

            setPolicy(sRefundAdvancePolicyComponent, false);
            enableOrDisableTimeSlot(false);
            adminTab = driver.getWindowHandle();

            if (iDriver == null) {
                Cls_Generic_Methods.switchToNewTab(driver.getWindowHandle());
                Cls_Generic_Methods.getURL(driver, Cls_Generic_Methods.getConfigValues("URL_" + env.toUpperCase()));
                Cls_Generic_Methods.waitForPageLoad(driver, 20);
                opdTab = driver.getWindowHandle();
            }

            switchToPolicyUser();
            createPatient();

            Cls_Generic_Methods.waitForElementsToBeDisplayed(oPage_Bills.list_previousAdvanceReceipts,10);

            if(oPage_Bills.list_previousAdvanceReceipts.size()==0){
                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_clickBills, 10);
                Cls_Generic_Methods.clickElement(oPage_Bills.button_clickBills);
                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_Bills.button_advanceReceiptBill,10);
                Cls_Generic_Methods.clickElement(oPage_Bills.button_advanceReceiptBill);

                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_advanceReceiptTemplate, 5);
                Cls_Generic_Methods.sendKeysIntoElement(oPage_PatientQueue.input_reasonAdvance, "Test");
                Cls_Generic_Methods.selectElementByVisibleText(oPage_PatientQueue.select_mop, "Cash");
                Cls_Generic_Methods.sendKeysIntoElement(oPage_PatientQueue.input_amountAdvance, "100");
                Cls_Generic_Methods.customWait();
                Cls_Generic_Methods.clickElement(oPage_PatientQueue.button_saveAdvance);
                Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_closeAdvance,10);
                Cls_Generic_Methods.clickElement(oPage_PatientQueue.button_closeAdvance);
                Cls_Generic_Methods.customWait();
            }

            Cls_Generic_Methods.clickElement(oPage_Bills.list_previousAdvanceReceipts.get(0));
            boolean advanceRefundBtnEnabled =Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_refundAdvance,10);
            m_assert.assertFalse(advanceRefundBtnEnabled, "<font color='blue'>Validated " + sPolicyDescription + " is disabled for the user</font>");

            switchToAdmin();
            setPolicy(sRefundAdvancePolicyComponent, true);
            Cls_Generic_Methods.customWait();

            switchToPolicyUser();
            Cls_Generic_Methods.driverRefresh();
            Cls_Generic_Methods.waitForPageLoad(driver, 20);
            selectPatientFromAllTab();

            Cls_Generic_Methods.waitForElementsToBeDisplayed(oPage_Bills.list_previousAdvanceReceipts,10);
            Cls_Generic_Methods.clickElement(oPage_Bills.list_previousAdvanceReceipts.get(0));

            advanceRefundBtnEnabled =Cls_Generic_Methods.waitForElementToBeDisplayed(oPage_PatientQueue.button_refundAdvance,10);
            m_assert.assertTrue(advanceRefundBtnEnabled, "<font color='blue'>Validated " + sPolicyDescription + " is enabled for the user</font>");

        } catch (Exception e) {
            e.printStackTrace();
            m_assert.assertFatal("Unable to validate " + sPolicyDescription + " ->" + e);
        }

    }


}
