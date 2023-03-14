package mac;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordEnumeration;
import java.io.InputStream;

public class DTMFRequestSony extends MIDlet implements CommandListener,
        Runnable {

    private TextBox dialTextBox, lunchTextBox, outTextBox, meetingTextBox,
    travelTextBox, sickTextBox, vacationTextBox, outForDayTextBox,
    logInTextBox, logOutTextBox;

    private Command DialCommand, ExitCommand, AboutCommand, statusCommand,
    goToBackInfoCommand,
    sendCommand, cancelCommand, lunchSendCommand, lunchBackCommand,
    groupcommand, sendCommand1, cancelCommand1,
    outSendCommand, outBackCommand, meetingSendCommand, meetingBackCommand,
    travelSendCommand, travelBackCommand,
    sickSendCommand, sickBackCommand, vacationSendCommand, vacationBackCommand,
    outForDaySendCommand, outForDayBackCommand, logInSendCommand,
    logInBackCommand, logOutSendCommand, logOutBackCommand, minimazeCommand,
    propertiesCommand, settingsCommand, helpCommand, backCommand,
    editSettingBackCommand,
    editSettingCancelCommand, editSettingSaveCommand;

    private Form editSettingForm;
    private Command thCmd;
    private String stringTotal;
    private int type = 0;
    private String SOS;
    private String setP = "/p"; //;postd= // /p
    private String accessCode, internNumber;
    private String SerieNumber = "00460101501594500"; // linus test > 35683100218981716, Emulator > 00460101501594500
    private String IMEI; // oscar > 35400700977717509

    private String identy, checkIdenty;
    private String sortString, Star;
    private String[] subStr;
    private String accessNumber, switchBoardNumber, star, setDayDate, setDate;
    private Alert alertEditSettings;

    private List list, groupList;

    public RecordStore recStore = null;
    static final String REC_STORE = "Data_Store_attendant_01";

    private TextField editUserName, editUserPassWord, editSmsNumber,
    editVidareKoppling, userName, userPassWord;

    private String JanM = "Jan", FebM = "Feb", MarM = "Mar", AprM = "Apr", MayM =
            "May", JunM = "Jun", JulM = "Jul", AugM = "Aug", SepM = "Sep", OctM =
                    "Oct", NovM = "Nov", DecM = "Dec";

    private String JanJ = "0", FebJ = "1", MarJ = "2", AprJ = "3", MajJ =
            "4", JunJ = "5", JulJ = "6", AugJ = "7", SepJ = "8", OktJ = "9",
    NovJ = "10", DecJ = "11";


    private int antalDagar = 30;
    private Date today;
    private TimeZone tz = TimeZone.getTimeZone("GMT+1");
    private DateField datefieldClock, datefieldDate;

    String s1;
    String s2;

    private String setMounth, setYear,
    DBdate, DBmounth, DByear, DBdateBack, DBmounthBack, DByearBack, getTWO,
    dateString, setViewMounth, ViewDateString, setdayBack,
    setmounthBack, setyearBack;


    private TextField dateNumber, accessNumbers, editSwitchBoardNumber;

    private int dayBack;
    private int mounthBack;
    private int yearBack;
    private int dayAfter;
    private int monthAfter;
    private int yearAfter;
    private int day;
    private int month;
    private int checkYear;


    public DTMFRequestSony() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        this.tz = tz;

        today = new Date();
        today.getTime();
        today.toString();

        System.out.println(today);

        this.antalDagar = antalDagar; // anger hur många dagar programmet ska vara öppet innan det stängs....

        try {
            this.accessNumber = getAccessNumber();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.switchBoardNumber = getSwitchBoardNumber();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        this.setDayDate = setDayDate;
        this.setDate = setDate;
        this.star = star;
        this.accessCode = accessCode;
        this.internNumber = internNumber;
        this.accessNumber = accessNumber;

        this.setP = setP;
        this.SOS = "112";
        this.identy = ""; // System.getProperty("com.sonyericsson.imei");
        this.checkIdenty = checkIdenty;
        this.SerieNumber = SerieNumber.trim();

        try {
            this.accessNumber = getAccessNumber();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.switchBoardNumber = getSwitchBoardNumber();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        this.accessCode = accessCode;
        this.internNumber = internNumber;
        this.accessNumber = accessNumber;

        this.IMEI = identy;
        this.star = "1";

        this.day = day;
        this.month = month;
        setDBDate(); // OBS.. Det här metodanropet ska ligga här efter month och day.
        setDBDateBack();


//---------------- EDITSETTINGFORM ---------------------------------------------

        editSettingForm = new Form("Egenskaper");

        dateNumber = new TextField("dagensdatum: ", "", 32, TextField.ANY);
        accessNumbers = new TextField("Accessnummer: ", "", 32,
                                      TextField.NUMERIC);

        editSwitchBoardNumber = new TextField("Växelnummer: ", "", 32,
                                              TextField.PHONENUMBER);

        AboutCommand = new Command("Om MexOS", Command.HELP, 5);
        helpCommand = new Command("Hjälp", Command.HELP, 4);

        editSettingBackCommand = new Command("Bakåt", Command.BACK, 1);
        editSettingCancelCommand = new Command("Avbryt", Command.BACK, 1);
        editSettingSaveCommand = new Command("Spara", Command.OK, 2);

        editSettingForm.addCommand(editSettingBackCommand);
        editSettingForm.addCommand(editSettingCancelCommand);
        editSettingForm.addCommand(editSettingSaveCommand);
        editSettingForm.setCommandListener(this);

//--------------- Alert-Screen -----------------------------------------

        alertEditSettings = new Alert("Sparar Ändringar",
                                      "\n\n\n...Ändringar sparas... ",
                                      null, AlertType.CONFIRMATION);
        setDataStore();
        upDateDataStore();
        alertEditSettings.setTimeout(2000);

        //--------------- Logout-textbox ---------------------------------------

        logOutTextBox = new TextBox("Skriv in Siffror", "", 4,
                                    TextField.NUMERIC);

        logOutSendCommand = new Command("Dial", Command.OK, 1);
        logOutBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        logOutTextBox.addCommand(logOutSendCommand);
        logOutTextBox.addCommand(logOutBackCommand);
        logOutTextBox.setCommandListener(this);

        //--------------- Login-textbox ---------------------------------------

        logInTextBox = new TextBox("Skriv in Siffror", "", 4, TextField.NUMERIC);

        logInSendCommand = new Command("Dial", Command.OK, 1);
        logInBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        logInTextBox.addCommand(logInSendCommand);
        logInTextBox.addCommand(logInBackCommand);
        logInTextBox.setCommandListener(this);

        //--------------- Lunch-textbox ---------------------------------------

        lunchTextBox = new TextBox("Skriv in HHMM", "", 4, TextField.NUMERIC);

        lunchSendCommand = new Command("Dial", Command.OK, 1);
        lunchBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        lunchTextBox.addCommand(lunchSendCommand);
        lunchTextBox.addCommand(lunchBackCommand);
        lunchTextBox.setCommandListener(this);

        //--------------- Tillfälligt ute -------------------------------------

        outTextBox = new TextBox("Skriv in HHMM", "", 4, TextField.NUMERIC);

        outSendCommand = new Command("Dial", Command.OK, 1);
        outBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        outTextBox.addCommand(outSendCommand);
        outTextBox.addCommand(outBackCommand);
        outTextBox.setCommandListener(this);
        //--------------- Sammanträde -------------------------------------

        meetingTextBox = new TextBox("Skriv in HHMM", "", 4, TextField.NUMERIC);

        meetingSendCommand = new Command("Dial", Command.OK, 1);
        meetingBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        meetingTextBox.addCommand(meetingSendCommand);
        meetingTextBox.addCommand(meetingBackCommand);
        meetingTextBox.setCommandListener(this);

        //--------------- Tjänsteresa -------------------------------------

        travelTextBox = new TextBox("Skriv in MMDD", "", 4, TextField.NUMERIC);

        travelSendCommand = new Command("Dial", Command.OK, 1);
        travelBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        travelTextBox.addCommand(travelSendCommand);
        travelTextBox.addCommand(travelBackCommand);
        travelTextBox.setCommandListener(this);

        //--------------- Sjuk -------------------------------------

        sickTextBox = new TextBox("Skriv in MMDD", "", 4, TextField.NUMERIC);

        sickSendCommand = new Command("Dial", Command.OK, 1);
        sickBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        sickTextBox.addCommand(sickSendCommand);
        sickTextBox.addCommand(sickBackCommand);
        sickTextBox.setCommandListener(this);

        //--------------- Semester -------------------------------------

        vacationTextBox = new TextBox("Skriv in MMDD", "", 4, TextField.NUMERIC);

        vacationSendCommand = new Command("Dial", Command.OK, 1);
        vacationBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        vacationTextBox.addCommand(vacationSendCommand);
        vacationTextBox.addCommand(vacationBackCommand);
        vacationTextBox.setCommandListener(this);

        //--------------- Gått för dagen -------------------------------------

        outForDayTextBox = new TextBox("Skriv in MMDD", "", 4,
                                       TextField.NUMERIC);

        outForDaySendCommand = new Command("Dial", Command.OK, 1);
        outForDayBackCommand = new Command("Bakåt", Command.BACK, 4); // Kommando hör till graphic-vyn för infot.

        outForDayTextBox.addCommand(outForDaySendCommand);
        outForDayTextBox.addCommand(outForDayBackCommand);
        outForDayTextBox.setCommandListener(this);

        //-------------- LISTA ------------------------------------------------
        list = new List("MexOS", Choice.EXCLUSIVE); // skapar en lista

        sendCommand = new Command("Hänvisa", Command.OK, 1);
        //DialCommand = new Command("Dial", Command.OK, 0);
        ExitCommand = new Command("Avsluta", Command.EXIT, 4);
        settingsCommand = new Command("Inställningar", Command.SCREEN, 3);
        minimazeCommand = new Command("Minimera", Command.SCREEN, 2);


        list.append("Lunch Åter", null); // attribut
        list.append("Tillfälligt Ute", null); // attribut
        list.append("Sammanträde", null); // attribut
        list.append("Tjänsteresa", null); // attribut
        list.append("Sjuk", null); // attribut
        list.append("Semester", null); // attribut
        list.append("Gått För Dagen", null); // attribut


        //list.addCommand(DialCommand);
        list.addCommand(ExitCommand);
        list.addCommand(settingsCommand);
        list.addCommand(sendCommand);
        list.addCommand(minimazeCommand);
        list.setCommandListener(this);

        //--------------Grupp LISTA -------------------------------------------
        groupList = new List("Grupper", Choice.EXCLUSIVE); // skapar en lista

        sendCommand1 = new Command("Ange", Command.OK, 3);
        cancelCommand1 = new Command("Bakåt", Command.BACK, 4);

        groupList.append("Inlogg alla grupper", null); // attribut
        groupList.append("Urlogg alla grupper", null); // attribut
        groupList.append("Inlogg grupp", null); // attribut
        groupList.append("Urlogg grupp", null); // attribut

        groupList.addCommand(sendCommand1);
        groupList.addCommand(cancelCommand1);
        groupList.setCommandListener(this);

        //validateIMEI(); // validerar IMEI-nummer, Om falsk stäng programmet...

        dialTextBox = new TextBox("MexOS ver 1.0", "", 30,
                                  TextField.PHONENUMBER);






        statusCommand = new Command("Hänvisning", Command.OK, 1);
        groupcommand = new Command("Grupper", Command.OK, 2);
        goToBackInfoCommand = new Command("Bakåt", Command.BACK, 4);


        //dialTextBox.addCommand(minimazeCommand);
        //dialTextBox.addCommand(settingsCommand);
        dialTextBox.addCommand(statusCommand);
        dialTextBox.addCommand(groupcommand);
        //dialTextBox.addCommand(ExitCommand);
        //dialTextBox.addCommand(DialCommand);
        dialTextBox.setCommandListener(this);

        //controllString();
            // controllDate();
                //this.ViewDateString = setViewDateString();

        if(ViewDateString == null){

            this.ViewDateString = "Enterprise License";

        }


    }


    public List getListElement() {

        return list;
    }

    public List getGroupList() {

        return groupList;
    }

    public TextBox getTextBoxOutForDay() {

        outForDayTextBox.setString("");

        return outForDayTextBox;
    }

    public TextBox getTextBoxVacation() {

        vacationTextBox.setString("");

        return vacationTextBox;
    }

    public TextBox getTextBoxSick() {

        sickTextBox.setString("");

        return sickTextBox;
    }

    public TextBox getTextBoxTravel() {

        travelTextBox.setString("");

        return travelTextBox;
    }

    public TextBox getTextBoxOut() {

        outTextBox.setString("");

        return outTextBox;
    }

    public TextBox getTextBoxMeeting() {

        meetingTextBox.setString("");

        return meetingTextBox;
    }

    public TextBox getTextBoxLunch() {

        lunchTextBox.setString("");

        return lunchTextBox;
    }

    public TextBox getLogInTextBox() {

        logInTextBox.setString("");

        return logInTextBox;
    }

    public TextBox getLogOutTextBox() {

        logOutTextBox.setString("");

        return logOutTextBox;
    }

    public void setLogInStatus() {

        String logIn = logInTextBox.getString();
        String loginA = "*28*";
        String loginB = logIn;
        String loginC = "%23";

        String loginD = loginA + loginB + loginC;

        this.stringTotal = loginD;

        accessCode = accessNumber;

    }

    public void setLogOutStatus() {

        String logOut = logOutTextBox.getString();
        String logOutA = "%2328*";
        String logOutB = logOut;
        String logOutC = "%23";

        String logOutD = logOutA + logOutB + logOutC;

        this.stringTotal = logOutD;

        accessCode = accessNumber;
    }

    public void setStatus(String attribut, String getInPutString) {

        String sendString = getInPutString;

        String setSend = attribut;
        String send = "";

        String one = "1";
        String two = "2";
        String three = "3";
        String four = "4";
        String five = "5";
        String six = "6";
        String seven = "7";

        if (setSend.equals(one)) {

            send = setSend;
        }
        if (setSend.equals(two)) {

            send = setSend;
        }
        if (setSend.equals(three)) {

            send = setSend;
        }
        if (setSend.equals(four)) {

            send = setSend;
        }
        if (setSend.equals(five)) {

            send = setSend;
        }
        if (setSend.equals(six)) {

            send = setSend;
        }
        if (setSend.equals(seven)) {

            send = setSend;
        }

        String C = "";
        C = sendString;
        String A = "*23*";
        String B = "%23";

        String D = A + send + C + B;

        this.stringTotal = D;

        accessCode = accessNumber;

    }

    public String sortCharAt(String s) {

        this.sortString = identy; // sortString innehåller samma som för IMEI-strängen för att kunna kontrollera å sortera bort tecken....

        StringBuffer bTecken = new StringBuffer(sortString); // Lägg strängen sortString i ett stringbuffer objekt...

        for (int i = 0; i < bTecken.length(); i++) { // räkna upp hela bTecken-strängens innehåll hela dess längd

            char tecken = bTecken.charAt(i); // char tecken är innehållet i hela längden

            if ('A' <= tecken && tecken <= 'Z' ||
                'a' <= tecken && tecken <= 'z' // Sorterar ur tecken ur IMEI-strängen
                || tecken == '-' || tecken == '/' || tecken == '\\' ||
                tecken == ':' || tecken == ';'
                || tecken == '.' || tecken == ',' || tecken == '_' ||
                tecken == '|' || tecken == '<'
                || tecken == '>' || tecken == '+' || tecken == '(' ||
                tecken == ')') {

                bTecken.setCharAt(i, ' '); // lägg in blanksteg i IMEI-strängen där något av ovanstående tecken finns....
            }

        }

        bTecken.append(' '); // lägger till blanksteg sist i raden så att sista kommer med för att do-satsen ska kunna hitta och sortera...

        String setString = new String(bTecken); // Gör om char-strängen till en string-sträng

        int antal = 0;
        char separator = ' '; // för att kunna sortera i do-satsen

        int index = 0;

        do { // do satsen sorterar ut blankstegen och gör en ny sträng för att jämföra IMEI med...
            ++antal;
            ++index;

            index = setString.indexOf(separator, index);
        } while (index != -1);

        subStr = new String[antal];
        index = 0;
        int slutindex = 0;

        for (int j = 0; j < antal; j++) {

            slutindex = setString.indexOf(separator, index);

            if (slutindex == -1) {
                subStr[j] = setString.substring(index);
            }

            else {
                subStr[j] = setString.substring(index, slutindex);
            }

            index = slutindex + 1;

        }
        String setNumber = "";
        for (int k = 0; k < subStr.length; k++) {

            setNumber += subStr[k]; // Lägg in värdena från subStr[k] i strängen setNumber....
        }

        System.out.println("Sorterad: " + setNumber);

        System.out.println("" + identy);

        String sendIMEI = setNumber;

        return sendIMEI;
    }

    public void validateIMEI() {
        //Identifierar serienumret IMEI...

        String validate = setIMEI(checkIdenty);

        if (!SerieNumber.equals(validate)) {

            System.out.println("FALSK");
            notifyDestroyed();

        } else {

            System.out.println("SANN");
        }

    }

    public String setIMEI(String totalIMEI) { // IMEI 00460101-501594-5-00

        String checkIMEI = "";

        totalIMEI = sortCharAt(checkIMEI);

        String TotalIMEI = totalIMEI;
        TotalIMEI.trim();

        return TotalIMEI;
    }


    public String toString(String b) {

        String s = b;

        return s;
    }

    public void startApp() {

        try {
            setDataStore();
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            upDateDataStore();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            controllString();
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreNotOpenException ex2) {
        } catch (RecordStoreException ex2) {
        }

        Display.getDisplay(this).setCurrent(list);
    }

    public void pauseApp() {

    }

    public void destroyApp(boolean unconditional) {

    }

    public void checkCountryNumber() { // Justerar landsiffra som är inmatad! Tar bort '+' och lägger in '00' före landssiffran

        String larmNummer = "112";
        String Number = "+";
        String setNumber = "00";
        String validate = dialTextBox.getString();
        String validate46 = "46";
        String setNumberNoll = "0";

        if (Number.equals(validate.substring(0, 1)) &&
            validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH '46' är sann så gör om till '0'

            accessCode = accessNumber;

            System.out.println("+46 är SANN gör om till 0 ");

            String setString = dialTextBox.getString();

            String deletePartOfString = setString.substring(3); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumberNoll + deletePartOfString; // sätt ihop strängen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer är : " + stringTotal);

        }
        if (Number.equals(validate.substring(0, 1)) &&
            !validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH 46 är falsk så gör om till '00'

            accessCode = accessNumber;

            System.out.println("Andra landsnummer tex +47 blir 00 SANN");

            String setString = dialTextBox.getString();

            String deletePartOfString = setString.substring(1); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumber + deletePartOfString; // sätt ihop strängen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer: " + stringTotal);

        }
        if (!Number.equals(validate.substring(0, 1))) { // ring vanligt nummer

            accessCode = accessNumber;

            this.stringTotal = dialTextBox.getString();

            System.out.println("Telefonnummer: " + stringTotal);

        }
        if (validate.equals(validate.substring(0, 1)) ||
            validate.equals(validate.substring(0, 2)) ||
            validate.equals(validate.substring(0, 3)) ||
            validate.equals(validate.substring(0, 4))) {

            accessCode = "";

            this.stringTotal = dialTextBox.getString();

            System.out.println("Internnummer: " + stringTotal);
        }
    }

    public void commandAction(Command c, Displayable d) { // SÄTTER COMMAND-ACTION STARTAR TRÄDETS KOMMANDON (trådar)
        Thread th = new Thread(this);
        thCmd = c;
        th.start();
    }

    public void run() {
        try {
            if (thCmd.getCommandType() == Command.EXIT) {
                notifyDestroyed();
            } else if (thCmd == sendCommand) { // eller om det är sendCommand så kör appliktationen
                int valet;
                valet = list.getSelectedIndex(); // OBS. ATT koden ser på 'getSelectedIndex' för att se vilket ELEMENT I LISTAN SOM ÄR VALET

                if (valet == 0) { // Lunch Åter

                    Display.getDisplay(this).setCurrent(getTextBoxLunch());

                } else if (valet == 1) { // TILLFÄLLIGT UTE

                    Display.getDisplay(this).setCurrent(getTextBoxOut());

                } else if (valet == 2) { // SAMMANTRÄDE

                    Display.getDisplay(this).setCurrent(getTextBoxMeeting());

                } else if (valet == 3) { // TJÄNSTERESA

                    Display.getDisplay(this).setCurrent(getTextBoxTravel());

                } else if (valet == 4) { // SJUK

                    Display.getDisplay(this).setCurrent(getTextBoxSick());

                } else if (valet == 5) { // SEMESTER

                    Display.getDisplay(this).setCurrent(getTextBoxVacation());

                } else if (valet == 6) { // GÅTT FÖR DAGEN

                    Display.getDisplay(this).setCurrent(getTextBoxOutForDay());

                } else if (valet == 7) { // Röstbrevlåda

                    try {// "#23%23"
                        String voicePost = "*59%23";
                        this.accessCode = accessNumber;
                        this.stringTotal = voicePost;

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}

                } else if (valet == 8) { // Ta bort hänvisning

                    try {
                        String deleteStatus = "%2323%23";
                        this.accessCode = accessNumber;
                        this.stringTotal = deleteStatus;

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}

                } else if (valet == 9) { // Nattkoppling

                    try {
                        String nightConnect = "*8%23";
                        this.accessCode = accessNumber;
                        this.stringTotal = nightConnect;

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}

                } else if (valet == 10) { // Inloggnin/Urloggning

                    try {
                        String logIn = "*28%23";
                        this.accessCode = accessNumber;
                        this.stringTotal = logIn;

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}

                }

            } else if (thCmd == sendCommand1) { // eller om det är sendCommand så kör appliktationen
                int valet1;
                valet1 = groupList.getSelectedIndex(); // OBS. ATT koden ser på 'getSelectedIndex' för att se vilket ELEMENT I LISTAN SOM ÄR VALET

                if (valet1 == 0) { // Inlogg alla grupper

                    try {
                        String logInGroup = "*28**%23";
                        this.accessCode = accessNumber;
                        this.stringTotal = logInGroup;

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}

                } else if (valet1 == 1) { // Urlogg alla grupper

                    try {
                        String logOutGroup = "%2328**%23";
                        this.accessCode = accessNumber;
                        this.stringTotal = logOutGroup;

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.

                    } catch (Exception e) {}

                } else if (valet1 == 2) { // Inlogg

                    Display.getDisplay(this).setCurrent(getLogInTextBox());

                } else if (valet1 == 3) { // Urlogg

                    Display.getDisplay(this).setCurrent(getLogOutTextBox());

                }

            } else if (thCmd == lunchBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == outBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == meetingBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == travelBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == sickBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == vacationBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == outForDayBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == logInBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(groupList);

            } else if (thCmd == logOutBackCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(groupList);

            } else if (thCmd == statusCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(getListElement());

            } else if (thCmd == groupcommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(getGroupList());

            } else if (thCmd == settingsCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                propertiesCommand = new Command("Egenskaper", Command.OK, 3);
                setDataStore();
                upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(propertiesCommand);
                k.addCommand(goToBackInfoCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);


            } else if (thCmd == editSettingBackCommand) { // Kommandot 'Tillbaka' hör till editSetting-Form

                propertiesCommand = new Command("Egenskaper", Command.OK, 3);
                setDataStore();
                upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(propertiesCommand);
                k.addCommand(goToBackInfoCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);


            } else if (thCmd == editSettingCancelCommand) { // Kommandot 'Logga Ut' hör till setting-Form

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == helpCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                backCommand = new Command("Bakåt", Command.OK, 2);

                Displayable k = new HelpInfo();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == AboutCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                backCommand = new Command("Bakåt", Command.OK, 2);

                Displayable k = new AboutUs();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == backCommand) { // Kommandot 'Tillbaka' hör till about-formen

                propertiesCommand = new Command("Egenskaper", Command.OK, 3);
                setDataStore();
                upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(propertiesCommand);
                k.addCommand(goToBackInfoCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);


            } else if (thCmd == editSettingSaveCommand) { // Kommandot 'Spara' hör till editSetting-Form

                openRecStore();
                setAccessNumber();
                setSwitchBoardNumber();
                closeRecStore();
                upDateDataStore();
                startApp();

                Display.getDisplay(this).setCurrent(alertEditSettings,
                        list);

            } else if (thCmd == propertiesCommand) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(getEditSettingForm());

            } else if (thCmd == sendCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                Display.getDisplay(this).setCurrent(getTextBoxLunch());

            } else if (thCmd == minimazeCommand) {

                Display.getDisplay(this).setCurrent(null);

            } else if (thCmd == goToBackInfoCommand) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(list);
            } // "tel:+46735708606/p9" >> p910/p990/m600i/N70/N80 // tel:+46851492163;postd=9 >> k700,k750,v600,s700,w810

            else if (thCmd == cancelCommand) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(dialTextBox);
            } else if (thCmd == cancelCommand1) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(list);

            } else if (thCmd == DialCommand) { //
                if (type == 0) {
                    try {
                        checkCountryNumber();
                        if (SOS.equals(stringTotal)) { // lunchSendCommand
                            platformRequest("tel:" + stringTotal.trim());
                        } else {

                            platformRequest("tel:" + switchBoardNumber + setP +
                                            accessCode + stringTotal.trim()); // dial the number > DTMF-signals.
                        } // <a href="tel:+4617613940;postd=12345">Dial</a>

                        dialTextBox.setString("");
                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        accessCode + stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == lunchSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttLunch = lunchTextBox.getString();
                        String one = "1";
                        setStatus(one, sendAttLunch);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        lunchTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == outSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttOut = outTextBox.getString();
                        String two = "2";
                        setStatus(two, sendAttOut);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        outTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == meetingSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttOut = meetingTextBox.getString();
                        String two = "3";
                        setStatus(two, sendAttOut);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        meetingTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == travelSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttOut = travelTextBox.getString();
                        String two = "4";
                        setStatus(two, sendAttOut);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        travelTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == sickSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttOut = sickTextBox.getString();
                        String two = "5";
                        setStatus(two, sendAttOut);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        sickTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == vacationSendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttOut = vacationTextBox.getString();
                        String two = "6";
                        setStatus(two, sendAttOut);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        vacationTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == outForDaySendCommand) { //
                if (type == 0) {
                    try {

                        String sendAttOut = outForDayTextBox.getString();
                        String two = "7";
                        setStatus(two, sendAttOut);
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        outForDayTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == logInSendCommand) { //
                if (type == 0) {
                    try {

                        setLogInStatus();

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        outForDayTextBox.setString("");
                        Display.getDisplay(this).setCurrent(list);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            } else if (thCmd == logOutSendCommand) { //
                if (type == 0) {
                    try {

                        setLogOutStatus();

                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // dial the number > DTMF-signals.
                        outForDayTextBox.setString("");
                        Display.getDisplay(this).setCurrent(dialTextBox);

                    } catch (Exception e) {}
                } else {
                    try {
                        platformRequest("tel:" + switchBoardNumber + setP +
                                        stringTotal.trim()); // open the wap browser.
                    } catch (Exception e) {}
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WMAMIDlet.run Exception " + e);
        }
    }

    //------------ D A T A - B A S - R M S -----------------------------------------

    public Form getEditSettingForm() { // METODEN RETURNERAR FORMEN FÖR EDITSETTINGS I EGENSKAPER

        editSettingForm.deleteAll();
        openRecStore();
        accessNumbers.setString(accessNumber);
        editSettingForm.append(accessNumbers);
        editSwitchBoardNumber.setString(switchBoardNumber);
        editSettingForm.append(editSwitchBoardNumber);
        closeRecStore();

        return editSettingForm;
    }

    // --- SET-metoder ------



    public void setDateNumber() {

        try {
            recStore.setRecord(3, dateNumber.getString().getBytes(), 0,
                               dateNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setAccessNumber() {

        try {
            recStore.setRecord(4, accessNumbers.getString().getBytes(), 0,
                               accessNumbers.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setSwitchBoardNumber() {
        try {
            recStore.setRecord(5, editSwitchBoardNumber.getString().getBytes(),
                               0,
                               editSwitchBoardNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setTWO() {
        try {

            openRecStore();
            String appt = "2";
            byte bytes[] = appt.getBytes();
            recStore.addRecord(bytes, 0, bytes.length);

            closeRecStore();
            upDateDataStore();
            startApp();

        } catch (Exception e) {
            // ALERT
        }
    }


    // ---- GET-metoder ---------

    public String getYear() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte a[] = recStore.getRecord(1);
        setYear = new String(a, 0, a.length);

        closeRecStore();

        return setYear;

    }

    public String getMounth() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte b[] = recStore.getRecord(2);
        setMounth = new String(b, 0, b.length);

        closeRecStore();

        return setMounth;

    }

    public String getDate() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte c[] = recStore.getRecord(3);
        setDate = new String(c, 0, c.length);

        closeRecStore();

        return setDate;

    }

    public String getAccessNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte d[] = recStore.getRecord(4);
        accessNumber = new String(d, 0, d.length);

        closeRecStore();

        return accessNumber;

    }


    public String getSwitchBoardNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte e[] = recStore.getRecord(5);
        switchBoardNumber = new String(e, 0, e.length);

        closeRecStore();

        return switchBoardNumber;

    }

    public String getThisYearBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte f[] = recStore.getRecord(6);
        setyearBack = new String(f, 0, f.length);

        closeRecStore();

        return setyearBack;

    }

    public String getThisMounthBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte g[] = recStore.getRecord(7);
        setmounthBack = new String(g, 0, g.length);

        closeRecStore();

        return setmounthBack;

    }

    public String getThisDayBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte h[] = recStore.getRecord(8);
        setdayBack = new String(h, 0, h.length);

        closeRecStore();

        return setdayBack;

    }


    public void getTWO() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        try {
            byte i[] = recStore.getRecord(9);
            getTWO = new String(i, 0, i.length);
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }

        try {
            this.dateString = getTWO;
        } catch (Exception ex1) {
        }

        System.out.println("häääääääääärrrrrrrr >>> getTWO >> " + getTWO);
        closeRecStore();

    }

    public void readRecordsUpdate() {
        try {
            System.out.println("Number of records: " + recStore.getNumRecords());

            if (recStore.getNumRecords() > 0) {
                RecordEnumeration re = recStore.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {
                    String str = new String(re.nextRecord());
                    System.out.println("Record: " + str);
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void readRecords() {
        try {
            // Intentionally small to test code below
            byte[] recData = new byte[5];
            int len;

            for (int i = 1; i <= recStore.getNumRecords(); i++) {
                // Allocate more storage if necessary
                if (recStore.getRecordSize(i) > recData.length) {
                    recData = new byte[recStore.getRecordSize(i)];
                }

                len = recStore.getRecord(i, recData, 0);
                if (Settings.debug) {
                    System.out.println("Record ID#" + i + ": " +
                                       new String(recData, 0, len));
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void writeRecord(String str) {
        byte[] rec = str.getBytes();

        try {
            System.out.println("sparar ");
            recStore.addRecord(rec, 0, rec.length);
            System.out.println("Writing record: " + str);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    public void openRecStore() {
        try {
            System.out.println("Öppnar databasen");
            // The second parameter indicates that the record store
            // should be created if it does not exist
            recStore = RecordStore.openRecordStore(REC_STORE, true);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void closeRecStore() {
        try {
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void setDataStore() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreNotOpenException,
            RecordStoreException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        if (recStore.getNumRecords() == 0) { // om innehållet i databasen är '0' så spara de tre första elementen i databasen.

            writeRecord(setYear);
            writeRecord(setMounth);
            writeRecord(setDate);
            writeRecord("0");
            writeRecord("+46");
            writeRecord(setyearBack);
            writeRecord(setmounthBack);
            writeRecord(setdayBack);

        }

        // sätter nummer i fönstret under inställningar...

        byte d[] = recStore.getRecord(4);
        accessNumber = new String(d, 0, d.length);

        byte e[] = recStore.getRecord(5);
        switchBoardNumber = new String(e, 0, e.length);

        closeRecStore();
    }

    // Om något inputfönster(post) i databasen är tom sätt tillbaka värdet...
    public void upDateDataStore() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();
        String setBackUserDateRecord = setDate;
        String setBackAccessNumberRecord = accessNumber;
        String setBackSwitchBoardNumberRecord = switchBoardNumber;

        if (recStore.getRecord(1) == null && recStore.getRecord(4) == null &&
            recStore.getRecord(5) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(1) == null && recStore.getRecord(4) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());

        } else if (recStore.getRecord(4) == null && recStore.getRecord(5) == null) {

            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(1) == null && recStore.getRecord(5) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(1) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
        } else if (recStore.getRecord(4) == null) {

            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
        } else if (recStore.getRecord(5) == null) {

            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        }

        closeRecStore();
    }


// ------------------- D A T U M -----------------------------------------------

    public void controllString() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        String readRecord;

        getTWO();

        readRecord = dateString;

        String viewRecord = readRecord;

        try {
            if (viewRecord.equals("2")) {

                notifyDestroyed();
            }
        } catch (Exception ex) {
        }
        System.out.println("VÄRDET PLATS 9 DB >> " + viewRecord);
    }

    public void controllDate() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        try {
            this.DBdate = getDate();
        } catch (RecordStoreNotOpenException ex) {
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            this.DBmounth = getMounth();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.DByear = getYear();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        try {
            this.DBdateBack = getThisDayBack();
        } catch (RecordStoreNotOpenException ex3) {
        } catch (InvalidRecordIDException ex3) {
        } catch (RecordStoreException ex3) {
        }
        try {
            this.DBmounthBack = getThisMounthBack();
        } catch (RecordStoreNotOpenException ex4) {
        } catch (InvalidRecordIDException ex4) {
        } catch (RecordStoreException ex4) {
        }
        try {
            this.DByearBack = getThisYearBack();
        } catch (RecordStoreNotOpenException ex5) {
        } catch (InvalidRecordIDException ex5) {
        } catch (RecordStoreException ex5) {
        }

        String useDBdate = DBdate.trim();
        String useDBmounth = DBmounth.trim();
        String useDByear = DByear.trim();

        String useDBdateBack = DBdateBack.trim();
        String useDBmounthBack = DBmounthBack.trim();
        String useDByearBack = DByearBack.trim();

        System.out.println("Skriver ut datum om 30 dagar >>> " + useDBdate);
        System.out.println("Skriver ut månad om 30 dagar >>> " + useDBmounth);
        System.out.println("Skriver ut året om 30 dagar >>> " + useDByear);

        System.out.println("Skriver ut Kontroll datum >>> " + useDBdateBack);
        System.out.println("Skriver ut Kontroll månad >>> " + useDBmounthBack);
        System.out.println("Skriver ut Kontroll år >>> " + useDByearBack);

        String toDayDate = checkDay().trim();
        String toDayMounth = checkMounth().trim();

        System.out.println("Skriver ut DAGENS DATUM >>> " + toDayDate);
        System.out.println("Skriver ut ÅRETS MÅNAD >>> " + toDayMounth);

        Integer controllDBdateBack = new Integer(0); // Gör om strängar till integer
        Integer controllDBmonthBack = new Integer(0); // Gör om strängar till integer
        Integer controllDByearBack = new Integer(0); // Gör om strängar till integer

        int INTDBdateBack = controllDBdateBack.parseInt(useDBdateBack);
        int INTDBmounthBack = controllDBmonthBack.parseInt(DBmounthBack);
        int INTDByearBack = controllDByearBack.parseInt(DByearBack);

        Integer controllDBdate = new Integer(0); // Gör om strängar till integer
        Integer controllDBmonth = new Integer(0); // Gör om strängar till integer
        Integer controllDByear = new Integer(0); // Gör om strängar till integer

        Integer controllToDayDBdate = new Integer(0); // Gör om strängar till integer
        Integer controllToDayDBmounth = new Integer(0); // Gör om strängar till integer

        int INTDBdate = controllDBdate.parseInt(useDBdate);
        int INTDBmounth = controllDBmonth.parseInt(DBmounth);
        int INTDByear = controllDByear.parseInt(DByear);

        int INTdateToDay = controllToDayDBdate.parseInt(toDayDate);
        int INTmounthToDay = controllToDayDBmounth.parseInt(toDayMounth);

        if (INTDBdate <= INTdateToDay && INTDBmounth <= INTmounthToDay &&
            INTDByear == checkYear) {

            System.out.println("SANN SANN SANN SANN SANN ");

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTmounthToDay == 0) { // Om INTmounthToDay har värdet '0' som är januari

            INTDBmounthBack = 0; // Då innehåller installations-månaden samma värde som nu-månaden.

        }
        if (INTDBmounthBack > INTmounthToDay) { // Om installations-månaden är större än 'dagens' månad som är satt i mobilen så stäng...

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDBmounthBack > INTmounthToDay && INTDByearBack < checkYear) { // Om installations-månaden är större än 'dagens' månad som är satt i mobilen så stäng...

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDByearBack > checkYear) { // Om installations-året är större än året som är satt i mobilen. >> går bakåt i tiden...

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDBdateBack > INTdateToDay && INTDBmounthBack > INTmounthToDay &&
            INTDByearBack > checkYear) {

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }
        if (INTDBmounthBack > INTmounthToDay && INTDByearBack > checkYear) {

            setTWO(); // Om månad och datum är sann skriv in "2" i databasen plats 6...

        }

    }


    public void setDBDate() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        countDay();

        System.out.println("Om 30 dagar är det den >> " + dayAfter +
                           ", och månad >> " + monthAfter + " det är år >> " +
                           yearAfter);

        String convertDayAfter = Integer.toString(dayAfter); // konvertera int till string...
        String convertMounthAfter = Integer.toString(monthAfter);
        String convertYearAfter = Integer.toString(yearAfter);

        this.setDate = convertDayAfter;
        this.setMounth = convertMounthAfter;
        this.setYear = convertYearAfter;

    }

    public void setDBDateBack() {

        countThisDay();

        System.out.println("Kontrollerar dagens dautm >> " + dayBack +
                           ", och månad >> " + mounthBack + " det är år >> " +
                           yearBack);

        String convertDayBack = Integer.toString(dayBack); // konvertera int till string...
        String convertMounthBack = Integer.toString(mounthBack);
        String convertYearBack = Integer.toString(yearBack);

        this.setdayBack = convertDayBack;
        this.setmounthBack = convertMounthBack;
        this.setyearBack = convertYearBack;

    }

    public void countThisDay() {

        // Get today's day and month
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        System.out.println("Dagens datum är den >> " + day +
                           ", Årets månad är nummer >> " + month +
                           " det är år >> " + year);

        this.dayBack = day;
        this.mounthBack = month;
        this.yearBack = year;

    }

    public void countDay() {

        // Get today's day and month
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        System.out.println("Dagens datum är den >> " + day +
                           ", Årets månad är nummer >> " + month +
                           " det är år >> " + year);
        this.checkYear = year;

        // Räknar fram 30 dagar framåt vilket datum år osv...
        final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
        long offset = date.getTime();
        offset += antalDagar * MILLIS_PER_DAY;
        date.setTime(offset);
        cal.setTime(date);

        // Now get the adjusted date back
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        year = cal.get(Calendar.YEAR);

        this.dayAfter = day;
        this.monthAfter = month;
        this.yearAfter = year;

    }

    private String regFromTextFile() { // Läser textfilen tmp.txt
        InputStream is = getClass().getResourceAsStream("tmp.txt");
        try {
            StringBuffer sb = new StringBuffer();
            int chr, i = 0;
            // Read until the end of the stream
            while ((chr = is.read()) != -1) {
                sb.append((char) chr);
            }

            return sb.toString();
        } catch (Exception e) {
            System.out.println("Unable to create stream");
        }
        return null;
    }

    public String setViewDateString() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        //ViewDateString

        String e1 = getDate();
        String e2 = setMounth();
        String e3 = getYear();

        ViewDateString = e1 + " " + e2 + " " + e3;

        return ViewDateString;

    }

    public String setMounth() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        setViewMounth = getMounth();

        if (setViewMounth.equals("0")) {

            this.setViewMounth = "Januari";
        }
        if (setViewMounth.equals("1")) {

            this.setViewMounth = "Februari";
        }
        if (setViewMounth.equals("2")) {

            this.setViewMounth = "Mars";
        }
        if (setViewMounth.equals("3")) {

            this.setViewMounth = "April";
        }
        if (setViewMounth.equals("4")) {

            this.setViewMounth = "Maj";
        }
        if (setViewMounth.equals("5")) {

            this.setViewMounth = "Juni";
        }
        if (setViewMounth.equals("6")) {

            this.setViewMounth = "Juli";
        }
        if (setViewMounth.equals("7")) {

            this.setViewMounth = "Augusti";
        }
        if (setViewMounth.equals("8")) {

            this.setViewMounth = "September";
        }
        if (setViewMounth.equals("9")) {

            this.setViewMounth = "Oktober";
        }
        if (setViewMounth.equals("10")) {

            this.setViewMounth = "November";
        }
        if (setViewMounth.equals("11")) {

            this.setViewMounth = "December";
        }

        String viewMounth = setViewMounth;

        return viewMounth;
    }

    public String checkDay() {

        String mobileClock = today.toString(); // Tilldelar mobileClock 'todays' datumvärde, skickar och gör om till en string av java.lang.string-typ

        String checkDayString = mobileClock.substring(8, 10); // plockar ut 'datum' tecken ur klockan

        if (checkDayString.equals("01")) {

            checkDayString = "1";

        } else if (checkDayString.equals("02")) {

            checkDayString = "2";

        } else if (checkDayString.equals("03")) {

            checkDayString = "3";

        } else if (checkDayString.equals("04")) {

            checkDayString = "4";

        } else if (checkDayString.equals("05")) {

            checkDayString = "5";

        } else if (checkDayString.equals("06")) {

            checkDayString = "6";

        } else if (checkDayString.equals("07")) {

            checkDayString = "7";

        } else if (checkDayString.equals("08")) {

            checkDayString = "8";

        } else if (checkDayString.equals("09")) {

            checkDayString = "9";

        }

        String useStringDate = checkDayString;

        return useStringDate;

    }

    public String checkMounth() {

        String mobileClock = today.toString(); // Tilldelar mobileClock 'todays' datumvärde, skickar och gör om till en string av java.lang.string-typ

        String checkMounthString = mobileClock.substring(4, 7); // plockar ut 'Månad' tecken ur klockan

        if (checkMounthString.equals("Jan")) {

            checkMounthString = "0";

        } else if (checkMounthString.equals("Feb")) {

            checkMounthString = "1";

        } else if (checkMounthString.equals("Mar")) {

            checkMounthString = "2";

        } else if (checkMounthString.equals("Apr")) {

            checkMounthString = "3";

        } else if (checkMounthString.equals("May")) {

            checkMounthString = "4";

        } else if (checkMounthString.equals("Jun")) {

            checkMounthString = "5";

        } else if (checkMounthString.equals("Jul")) {

            checkMounthString = "6";

        } else if (checkMounthString.equals("Aug")) {

            checkMounthString = "7";

        } else if (checkMounthString.equals("Sep")) {

            checkMounthString = "8";

        } else if (checkMounthString.equals("Oct")) {

            checkMounthString = "9";

        } else if (checkMounthString.equals("Nov")) {

            checkMounthString = "10";

        } else if (checkMounthString.equals("Dec")) {

            checkMounthString = "11";

        }

        String useStringMounth = checkMounthString;

        return useStringMounth;

    }


}
