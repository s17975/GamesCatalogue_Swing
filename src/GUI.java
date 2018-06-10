import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Marcin Rudko
 * @version 31.05.2018
 **/

public class GUI extends JFrame {

    //    ------------------------------------
    //    USTAWIENIA RAMKI -> [ROZMIAR EKRANU] [ROZMIAR RAMKI] [IKONA PROGRAMU]
    //    ------------------------------------

    private Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();                                                        //->[ROZMIAR EKRANU]
    private Dimension frameSize = new Dimension(scrSize.width-(scrSize.width/10),scrSize.height-(scrSize.height/8));  //->[ROZMIAR RAMKI]
    private Image programIconImage = new ImageIcon("programIcon.png").getImage();                                          //->[IKONA PROGRAMU]

    //    ------------------------------------
    //    KOMPONENTY WIDOKU -> [PRZYCISKI]
    //    ------------------------------------

    public JButton addGameButton;                               //-> Przycisk, dodaj nową grę
    public JButton removeGameButton;                            //-> Przycisk, usuń zaznaczone gry
    public JButton loadGamesButton;                             //-> Przycisk, wczytaj plik z grami
    public JButton saveGamesButton;                             //-> Przycisk, zapisz gry do pliku
    public JButton createNewGameObject;                         //-> Przycisk, swtwórz obiekt nowej gry
    public JButton filterButton;                                //-> Przycisk, filtruj dane modelu
    public JButton unfilterButton;                              //-> Przycisk, wyczyść filtrowanie danych modelu
    public JButton removeAll;

    //    ------------------------------------
    //    KOMPONENTY WIDOKU -> [POLA DO WPROWADZANIA WARTOŚCI NOWEJ GRY]
    //    ------------------------------------

    public JTextField textFieldProducer;                        //-> Pole [String], wprowadź producenta
    public JTextField textFieldTitle;                           //-> Pole [String], wprowadź tytuł
    public JTextField textFieldCategory;                        //-> Pole [String], wprowadź kategorię
    public JFormattedTextField textFieldNoOfPlayers;            //-> Pole [integer], wprowadź liczbę graczy
    public JFormattedTextField textFieldPrice;                  //-> Pole [float], wprowadź cenę

    //    ------------------------------------
    //    KOMPONENTY WIDOKU -> [POLA DO FILTROWANIA TABELI]
    //    ------------------------------------

    public JComboBox filterTitle;                                 //-> ComboBox [String], wybrany tytuł
    public JComboBox filterProducer;                              //-> ComboBox [String], wybrany producent
    public JComboBox filterCategory;                              //-> ComboBox [String], wybrana kategoria
    public JComboBox filterNoOfPlayers;                           //-> ComboBox [String], wybrana ilość graczy
    public JFormattedTextField filterPriceFROM;                   //-> ComboBox [float], wybrana cena od
    public JFormattedTextField filterPriceTO;                     //-> ComboBox [float], wybrana cena do
    public JTable jTableData;                                     //-> Tabela [JTable], wyświetlająca dane ustawionego modelu

    //    ------------------------------------
    //    KOMPONENTY WIDOKU -> [WYBÓR PLIKU DO ZAPISU/ODCZYTU]
    //    ------------------------------------

    public JFileChooser saveFileChooser;                        //-> FileChooser, zapis pliku
    public JFileChooser loadFileChooser;                        //-> FileChooser, wczytanie pliku

    //    ------------------------------------
    //    KONSTRUKTOR TWORZĄCY RAMKĘ -> [mainFrame]
    //    ------------------------------------

    public GUI(){
        this.mainFrame();
    }

    //    ------------------------------------
    //    GŁÓWNA RAMKA PROGRAMU -> [mainFrame]
    //    ------------------------------------

    private JFrame mainFrame(){
        this.setTitle("Katalog gier planszowych");
        this.setMinimumSize(frameSize);
        this.setResizable(true);
        this.setLocationRelativeTo(null);                        //-> Ustaw położenie ramki programu
        this.setIconImage(programIconImage);                     //-> Ustaw ikonę programu
        this.setContentPane(contentPanel());                     //-> Ustaw zawartość ramki programu
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        return this;
    }

    //    ------------------------------------
    //    GŁÓWNY PANEL PROGRAMU -> [contentPanel]
    //    ------------------------------------

    private JPanel contentPanel(){
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(true);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPanel.setLayout(new BorderLayout(5,5));
        contentPanel.add(viewPanel(), BorderLayout.PAGE_START);                                     //-> Dodaj panel z tabelą danych [viewPanel]
        contentPanel.add(filteringPanel(), BorderLayout.CENTER);                                    //-> Dodaj panel z przyciskami [controlPanel]
        contentPanel.add(controlPanel(),BorderLayout.PAGE_END);                                     //-> Dodaj panel z filtrowaniem [filteringPanel]
        return contentPanel;
    }

    //    ------------------------------------
    //    PANEL Z TABELĄ DANYCH -> [viewPanel]
    //    ------------------------------------

    private JPanel viewPanel(){
        this.jTableData = new JTable();
        this.jTableData.setEnabled(true);
        this.jTableData.setAutoCreateRowSorter(true);                                     //-> Autosortowanie kolumn włączone, konieczne nadpisanie metody [getColumnClass]
        JScrollPane scrollPane = new JScrollPane(jTableData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JPanel viewPanel = new JPanel();
        viewPanel.setBorder(BorderFactory.createTitledBorder(" Lista dostępnych gier "));
        viewPanel.setOpaque(true);
        viewPanel.setBackground(Color.WHITE);
        viewPanel.setLayout(new BorderLayout(5,5));
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        viewPanel.add(scrollPane);
        return viewPanel;
    }

    //    ------------------------------------
    //    PANEL ZARZĄDZAJĄCY DANYMI MODELU -> [controlPanel]
    //    ------------------------------------

    protected JPanel controlPanel(){
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createTitledBorder(" Zarządzaj danymi "));
        controlPanel.setBackground(Color.WHITE);

        JButton removeGameButton = new JButton("Usuń grę");                          //-> Przycisk, Usuń grę
        removeGameButton.setEnabled(false);                                               //-> Ustaw przycisk jako nieaktywny
        this.removeGameButton = removeGameButton;

        JButton addGameButton = new JButton("Dodaj grę");                            //-> Przycisk, Dodaj grę
        addGameButton.setEnabled(true);                                                   //-> Ustaw przycisk jako aktywny
        this.addGameButton = addGameButton;

        JButton loadGamesButton = new JButton("Wczytaj listę gier");                 //-> Przycisk, Wczytaj plik z grami
        loadGamesButton.setEnabled(true);                                                 //-> Ustaw przycisk jako aktywny
        this.loadGamesButton = loadGamesButton;

        JButton saveGamesButton = new JButton("Zapisz listę gier");                  //-> Przycisk, Zapisz plik z grami
        saveGamesButton.setEnabled(false);                                                //-> Ustaw przycisk jako nieaktywny
        this.saveGamesButton = saveGamesButton;

        JButton removeAll = new JButton("Usuń wszystko");
        removeAll.setEnabled(true);
        this.removeAll = removeAll;

        controlPanel.add(addGameButton);
        controlPanel.add(removeGameButton);
        controlPanel.add(loadGamesButton);
        controlPanel.add(saveGamesButton);
        controlPanel.add(removeAll);
        return controlPanel;
    }

    //    ------------------------------------
    //    PANEL FILTRUJĄCY DANE MODELU -> [filteringPanel]
    //    ------------------------------------

    protected JPanel filteringPanel(){
        JPanel filteringPanel = new JPanel();
        filteringPanel.setBorder(BorderFactory.createTitledBorder(" Opcje wyszukiwania "));
        filteringPanel.setBackground(Color.WHITE);
        String[] defaultFilteringValue = {"wszystko"};                                      //-> Domyślna wartość wyświetlana w filtrach wyboru

        JLabel labelTitle = new JLabel("Tytuł:");                                    // |
        JLabel labelProducer = new JLabel("Producent:");                             // |
        JLabel labelCategory = new JLabel("Kategoria:");                             // | ETYKIETY
        JLabel labelNoOfPlayers = new JLabel("Liczba Graczy:");                      // | FILTRÓW
        JLabel labelPriceFrom = new JLabel("Cena od:");                              // |
        JLabel labelPriceTo = new JLabel("Cena do:");                                // |

        JPanel panelTitle = new JPanel();                                                 // |
        JPanel panelProducer = new JPanel();                                              // |
        JPanel panelCategory = new JPanel();                                              // | PANELE
        JPanel panelNoOfPlayers = new JPanel();                                           // | FILTRÓW
        JPanel panelPriceFrom = new JPanel();                                             // |
        JPanel panelPriceTo = new JPanel();                                               // |

        JComboBox filterTitle = new JComboBox(defaultFilteringValue);                     //-> Filtr [Tytuł] z domyślną wartością [defaultFilteringValue]
        this.filterTitle = filterTitle;
        panelTitle.add(labelTitle);
        panelTitle.add(this.filterTitle);

        JComboBox filterProducer = new JComboBox(defaultFilteringValue);                  //-> Filtr [Producent] z domyślną wartością [defaultFilteringValue]
        this.filterProducer = filterProducer;
        panelProducer.add(labelProducer);
        panelProducer.add(this.filterProducer);

        JComboBox filterCategory = new JComboBox(defaultFilteringValue);                  //-> Filtr [Kategoria] z domyślną wartością [defaultFilteringValue]
        this.filterCategory = filterCategory;
        panelCategory.add(labelCategory);
        panelCategory.add(this.filterCategory);

        JComboBox filterNoOfPlayers = new JComboBox(defaultFilteringValue);               //-> Filtr [Liczba Graczy] z domyślną wartością [defaultFilteringValue]
        this.filterNoOfPlayers = filterNoOfPlayers;
        panelNoOfPlayers.add(labelNoOfPlayers);
        panelNoOfPlayers.add(this.filterNoOfPlayers);

        JFormattedTextField filterPriceFROM = new JFormattedTextField(setPriceFormat());  //-> Filtr [Cena Do] z formatowaniem [setPriceFormat()]
        filterPriceFROM.setColumns(4);
        this.filterPriceFROM = filterPriceFROM;
        panelPriceFrom.add(labelPriceFrom);
        panelPriceFrom.add(this.filterPriceFROM);

        JFormattedTextField filterPriceTO = new JFormattedTextField(setPriceFormat());    //-> Filtr [Cena Do] z formatowaniem [setPriceFormat()]
        filterPriceTO.setColumns(4);
        this.filterPriceTO = filterPriceTO;
        panelPriceTo.add(labelPriceTo);
        panelPriceTo.add(this.filterPriceTO);

        filteringPanel.add(panelTitle);                                                   // |
        filteringPanel.add(panelProducer);                                                // |
        filteringPanel.add(panelCategory);                                                // | DODAJ
        filteringPanel.add(panelNoOfPlayers);                                             // | PANELE
        filteringPanel.add(panelPriceFrom);                                               // | FILTROWANIA
        filteringPanel.add(panelPriceTo);                                                 // |

        JButton filterButton = new JButton("Filtruj");                               //-> Przycisk, [Filtruj dane]
        this.filterButton = filterButton;
        this.filterButton.setEnabled(false);                                              //-> Ustaw przycisk jako nieaktywny

        JButton unfilterButton = new JButton("Wyczyść");                             //-> Przycisk, [Wyczyść filtry]
        this.unfilterButton = unfilterButton;
        this.unfilterButton.setEnabled(false);                                            //-> Ustaw przycisk jako nieaktywny

        filteringPanel.add(this.filterButton);                                            // | DODAJ
        filteringPanel.add(this.unfilterButton);                                          // | PRZYCISKI FILTROWANIA
        return filteringPanel;
    }

    //    ------------------------------------
    //    PANEL WYBORU PLIKU DO ZAPISU I WCZYTANIA -> [selectFileDialog]
    //    ------------------------------------

    public void selectFileDialog(ActionEvent event){
        JPanel panel = new JPanel();
        this.saveFileChooser = new JFileChooser();
        this.loadFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        this.loadFileChooser.setFileFilter(filter);

        if (event.getSource()==loadGamesButton){                                         //-> Sprawdza źródło akcji kliknięcia przycisku
            loadFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);    //   i wywołuje odpowidni FileChooser
            panel.add(loadFileChooser);
            loadFileChooser.showOpenDialog(panel);
        }
        else {
            saveFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            panel.add(saveFileChooser);
            saveFileChooser.showSaveDialog(panel);
        }
    }

    //    ------------------------------------
    //    PANEL TWORZENIA NOWEJ GRY -> [newGameDialog]
    //    ------------------------------------

    public void newGameDialog(){
        JDialog createGameDialog = new JDialog();
        JPanel createGamePanel = new JPanel();
        createGamePanel.setLayout(new GridLayout(6,1,2,2));

        JLabel labelProducer = new JLabel("Producent: ");                        //-> Panel [Producent]
        JTextField textFieldProducer = new JTextField(20);
        this.textFieldProducer = textFieldProducer;
        JPanel panelProducer = new JPanel();
        panelProducer.add(labelProducer);
        panelProducer.add(textFieldProducer);

        JLabel labelTitle = new JLabel("Tytuł: ");                               //-> Panel [Tytuł]
        JTextField textFieldTitle = new JTextField(20);
        this.textFieldTitle = textFieldTitle;
        JPanel panelTitle = new JPanel();
        panelTitle.add(labelTitle);
        panelTitle.add(textFieldTitle);

        JLabel labelCategory = new JLabel("Kategoria: ");                        //-> Panel [Kategoria]
        JTextField textFieldCategory = new JTextField(20);
        this.textFieldCategory = textFieldCategory;
        JPanel panelCategory = new JPanel();
        panelCategory.add(labelCategory);
        panelCategory.add(textFieldCategory);

        JLabel labelNoOfPlayers = new JLabel("Liczba graczy: ");                 //-> Panel [Liczba Graczy]
        JFormattedTextField textFieldNoOfPlayers = new JFormattedTextField( setNoOfPlayersFormat() );
        this.textFieldNoOfPlayers = textFieldNoOfPlayers;
        textFieldNoOfPlayers.setColumns(10);
        JPanel panelNoOfPlayers = new JPanel();
        panelNoOfPlayers.add(labelNoOfPlayers);
        panelNoOfPlayers.add(textFieldNoOfPlayers);

        JLabel labelPrice = new JLabel("Cena: ");                                //-> Panel [Cena]
        JFormattedTextField textFieldPrice = new JFormattedTextField( setPriceFormat() );
        this.textFieldPrice = textFieldPrice;
        textFieldPrice.setColumns(10);
        JPanel panelPrice = new JPanel();
        panelPrice.add(labelPrice);
        panelPrice.add(textFieldPrice);

        JPanel panelButton = new JPanel();                                            //-> Panel z przyciskiem [Dodaj grę]
        JButton createNewGame = new JButton("DODAJ");
        this.createNewGameObject = createNewGame;
        createNewGame.setEnabled(true);
        panelButton.add(createNewGame);

        createGamePanel.add(panelProducer);
        createGamePanel.add(panelTitle);
        createGamePanel.add(panelCategory);
        createGamePanel.add(panelNoOfPlayers);
        createGamePanel.add(panelPrice);
        createGamePanel.add(panelButton);

        createGamePanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10,10,10,10), BorderFactory.createEtchedBorder()));
        createGameDialog.setContentPane(createGamePanel);
        createGameDialog.setTitle("Dodaj nową grę");
        createGameDialog.setSize(scrSize.width/2,scrSize.height/2);
        createGameDialog.setLocationRelativeTo(null);
        createGameDialog.setResizable(false);
        createGameDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createGameDialog.setVisible(true);
    }

    //    ------------------------------------
    //    FORMATTER DO FILTROWANIA DANYCH WEJŚCIOWYCH DLA PÓL DO WPROWADZANIA CENY -> [setPriceFormat]
    //    ------------------------------------

    public NumberFormatter setPriceFormat(){
        NumberFormat floatFormat = DecimalFormat.getInstance();
        floatFormat.setMinimumIntegerDigits(0);
        floatFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
        floatFormat.setMinimumFractionDigits(2);
        floatFormat.setMaximumFractionDigits(2);
        NumberFormatter floatFormatter = new NumberFormatter(floatFormat);
        floatFormatter.setAllowsInvalid(false);
        return floatFormatter;
    }

    //    ------------------------------------
    //    FORMATTER DO FILTROWANIA DANYCH WEJŚCIOWYCH DLA POLA DO WPROWADZANIA LICZBY GRACZY -> [setNoOfPlayersFormat]
    //    ------------------------------------

    public NumberFormatter setNoOfPlayersFormat(){
        NumberFormat intFormat = NumberFormat.getInstance();
        intFormat.setMinimumIntegerDigits(0);
        intFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
        intFormat.setMinimumFractionDigits(0);
        intFormat.setMaximumFractionDigits(0);
        NumberFormatter intFormatter = new NumberFormatter(intFormat);
        intFormatter.setAllowsInvalid(false);
        return intFormatter;
    }
}
