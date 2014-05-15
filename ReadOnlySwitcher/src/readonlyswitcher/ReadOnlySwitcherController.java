/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readonlyswitcher;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

/**
 * FXML Controller class
 *
 * @author cruiserupce
 */
public class ReadOnlySwitcherController implements Initializable {

    private Stage stage;
    private static final WinReg.HKEY HKEY = WinReg.HKEY_LOCAL_MACHINE;
    private static final String REGISTERROOT = "System\\CurrentControlSet\\Control";
    private static final String REGISTER = REGISTERROOT + "\\StorageDevicePolicies";
    private static final String DWORD = "WriteProtect";
    private static final String INTRO = "Edit register HKEY_LOCAL_MACHINE\\System\\CurrentControlSet\\Control\\StorageDevicePolicies\n\nCreat new DWORD called WriteProtect and give it a value of 1 for Read only or 0 for Read/Write\n\nThis is apply only to devices mounted after thic command.\n\nSet register to Read only and mount device after that.";

    private static final String INTRO_ERROR = "Please run regedit.exe and create key:\n\nHKEY_LOCAL_MACHINE\\System\\CurrentControlSet\\Control\\StorageDevicePolicies \n\nThen please go to properties of this key and set full control to Users and restart this program.";
    private Boolean rOnly;
    @FXML
    private RadioButton readOnly;
    @FXML
    private ToggleGroup group1;
    @FXML
    private RadioButton readWrite;
    @FXML
    private TextArea text;
    @FXML
    private ImageView imageUnlock;
    @FXML
    private ImageView imageLock;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onLoad();
    }

    private void onLoad() {
        text.setText(INTRO);
        Integer value = null;
        List<String> listControl = Arrays.asList(Advapi32Util.registryGetKeys(HKEY, REGISTERROOT));
        if (!listControl.contains("StorageDevicePolicies")) {
            text.setText(INTRO_ERROR);
        } else {
            List<String> listStorageDevicePolicies = Arrays.asList(Advapi32Util.registryGetKeys(HKEY, REGISTER));
            if (listStorageDevicePolicies.contains(DWORD)) {
                value = Advapi32Util.registryGetIntValue(HKEY, REGISTER, DWORD);
            } else {
//                Advapi32Util.registryCreateKey(HKEY, REGISTER, DWORD);
//                Advapi32Util.registrySetStringValue(HKEY, REGISTER, DWORD, "1");
                Advapi32Util.registrySetIntValue(HKEY, REGISTER, DWORD, 1);
                value = Advapi32Util.registryGetIntValue(HKEY, REGISTER, DWORD);
            }
        }
        if (value != null) {
            this.setReadOnly(1==value);
        }
        if (rOnly != null) {
            if (rOnly) {
                readOnly.setSelected(true);
            } else {
                readWrite.setSelected(true);
            }
        } else {
            readOnly.setDisable(true);
            readWrite.setDisable(true);
        }
    }

    public void setReadOnly(Boolean rOnly) {
        if (rOnly) {
            imageLock.setVisible(true);
            imageUnlock.setVisible(false);
        } else {
            imageLock.setVisible(false);
            imageUnlock.setVisible(true);
        }
        this.rOnly = rOnly;
    }

    public Boolean isReadOnly() {
        return rOnly;
    }

    public boolean writeToRegister() {
        Integer value;
        if (rOnly) {
            Advapi32Util.registrySetIntValue(HKEY, REGISTER, DWORD, 1);
//            Advapi32Util.registrySetStringValue(HKEY, REGISTER, DWORD, "1");
            value = Advapi32Util.registryGetIntValue(HKEY, REGISTER, DWORD);
            return 1==value;
        } else {
            Advapi32Util.registrySetIntValue(HKEY, REGISTER, DWORD, 0);
//            Advapi32Util.registrySetStringValue(HKEY, REGISTER, DWORD, "0");
            value = Advapi32Util.registryGetIntValue(HKEY, REGISTER, DWORD);
            return 0==value;
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void handleRO() {
        this.setReadOnly(true);
        if (!this.writeToRegister()) {
            this.setReadOnly(false);
            text.setText("Error occur while setting ReadOnly");
        }
    }

    private void handleRW() {
        this.setReadOnly(false);
        if (!this.writeToRegister()) {
            this.setReadOnly(true);
            text.setText("Error occur while setting ReadWrite");
        }
    }

    @FXML
    private void readOnlySet(MouseEvent event) {
        handleRO();
    }

    @FXML
    private void readWriteSet(MouseEvent event) {
        handleRW();
    }

}
