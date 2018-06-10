import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * @author Marcin Rudko
 * @version 31.05.2018
 **/

public class Controler {
    protected GUI view;
    protected Model model;

    //    ------------------------------------
    //    KONSTRUKTOR INICJUJĄCY ELEMENTY WIDOKU ORAZ MODELU
    //    ------------------------------------

    public Controler(GUI view, Model model){
        this.view = view;
        this.model = model;

        setViewJTableModel(this.model);                        //-> Ustawia model danych do tabeli widoku [view.jTableData]
        setViewJTableSorter(this.model.sorter);                //-> Ustawia sorter danych do tabeli widoku [view.jTableData]
        setSortingComponentsValues();                          //-> Ustawia początkowe wartości elementów filtrujących dane modelu [model]
        setViewButtonActions();
    }

    //    ------------------------------------
    //    USTAW DANE MODELU WYŚWIETLANEGO W TABELI WIDOKU
    //    ------------------------------------

    public void setViewJTableModel(Model tableModel){
        this.view.jTableData.setModel(tableModel);
    }

    //    ------------------------------------
    //    USTAW SORTER MODELU WYŚWIETLANEGO W TABELI WIDOKU
    //    ------------------------------------

    private void setViewJTableSorter(TableRowSorter<Model> tableSorter){
        this.view.jTableData.setRowSorter(tableSorter);
    }

    //    ------------------------------------
    //    USTAW WARTOŚCI WYBORU KOMPONENTÓW FILTRUJĄCYCH DANE MODELU
    //    ------------------------------------

    public void setSortingComponentsValues(){
        this.model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                setSortingComponentValues(view.filterTitle,1);
                setSortingComponentValues(view.filterProducer,2);
                setSortingComponentValues(view.filterCategory,3);
                setSortingComponentValues(view.filterNoOfPlayers,4);
            }
        });
    }

    //    ------------------------------------
    //    USTAW UNIKATOWE WARTOŚCI DANYCH MODELU W KOMPONENTACH FILTRUJĄCYCH DANE MODELU
    //    ------------------------------------

    public void setSortingComponentValues(JComboBox sortingComponent, int columnIndex){
        sortingComponent.removeAllItems();
        sortingComponent.addItem("wszystko");
        for (int i = 0; i< getColumnUniqueItems(columnIndex).size(); i++){
            sortingComponent.addItem(getColumnUniqueItems(columnIndex).toArray()[i]);
        }
    }

    //    ------------------------------------
    //    POBIERZ UNIKATOWE WARTOŚCI Z DANYCH MODELU WYBRANEJ KOLUMNY
    //    ------------------------------------

    public TreeSet getColumnUniqueItems(int column){
        TreeSet set = new TreeSet();
        for(int i = 0; i<model.getRowCount(); i++){
            if(!set.add(model.getValueAt(i,column))){
            }
        }
        return set;
    }

    //    ------------------------------------
    //    SPRAWDZA POPRAWNOŚĆ WPROWADZONYCH DANYCH W PANELU TWORZENIA NOWEJ GRY
    //    ------------------------------------

    public boolean checkAllFields(){
        if(view.textFieldTitle.getText().isEmpty())
            return false;
        else if (view.textFieldProducer.getText().isEmpty())
            return false;
        else if (view.textFieldCategory.getText().isEmpty())
            return false;
        else if (view.textFieldNoOfPlayers.getText().isEmpty())
            return false;
        else if (view.textFieldPrice.getText().isEmpty())
            return false;
        else return true;
    }

    //    ------------------------------------
    //    TWORZY LISTĘ FILTROWANIA DANYCH MODELU W OPARCIU O WYBRANE WARTOŚCI KOMPONENTÓW FILTROWANIA
    //    ------------------------------------

    public List<RowFilter<Model, Object>> MyRowFilter(){
        if (model.getRowCount() > 0) {

            List<RowFilter<Model, Object>> listOfFilters = new ArrayList<>();

            if (!view.filterTitle.getSelectedItem().toString().equals("wszystko")) {
                String TitleTextFilter = "^" + view.filterTitle.getSelectedItem().toString() + "$";
                RowFilter<Model, Object> TitleRowFilter = RowFilter.regexFilter(TitleTextFilter, 1);
                listOfFilters.add(TitleRowFilter);
            }
            if (!view.filterProducer.getSelectedItem().toString().equals("wszystko")) {
                String ProducerTextFilter = "^" + view.filterProducer.getSelectedItem().toString() + "$";
                RowFilter<Model, Object> ProducerRowFilter = RowFilter.regexFilter(ProducerTextFilter, 2);
                listOfFilters.add(ProducerRowFilter);
            }
            if (!view.filterCategory.getSelectedItem().toString().equals("wszystko")) {
                String CategoryTextFilter = "^" + view.filterCategory.getSelectedItem().toString() + "$";
                RowFilter<Model, Object> CategoryRowFilter = RowFilter.regexFilter(CategoryTextFilter, 3);
                listOfFilters.add(CategoryRowFilter);
            }
            if (!view.filterNoOfPlayers.getSelectedItem().toString().equals("wszystko")) {
                String NoOfPlayersTextFilter = "^" + view.filterNoOfPlayers.getSelectedItem().toString() + "$";
                RowFilter<Model, Object> NoOfPlayersRowFilter = RowFilter.regexFilter(NoOfPlayersTextFilter, 4);
                listOfFilters.add(NoOfPlayersRowFilter);
            }
            if (view.filterPriceFROM.getValue() != null) {
                String stringPriceFROM = view.filterPriceFROM.getValue().toString().replace(",", ".");
                RowFilter<Model, Object> PriceFromRowFilter = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, Float.parseFloat(stringPriceFROM), 5);
                listOfFilters.add(PriceFromRowFilter);
            }
            if (view.filterPriceTO.getValue() != null) {
                String stringPriceTO = view.filterPriceTO.getValue().toString().replace(",", ".");
                RowFilter<Model, Object> PriceToRowFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, Float.parseFloat(stringPriceTO), 5);
                listOfFilters.add(PriceToRowFilter);
            }
            return listOfFilters;
        }
        List<RowFilter<Model, Object>> listOfFilters = new ArrayList<>();
        return listOfFilters;
    }

    //    ------------------------------------
    //    USUWA FILTROWANIE DANYCH MODELU
    //    ------------------------------------

    public void removeRowFilter(){
        this.model.sorter.setRowFilter(null);
    }

    //    ------------------------------------
    //    USTAWIA AKCJE PRZYCISKÓW WIDOKU
    //    ------------------------------------

    public void setViewButtonActions(){

        //    DODAJ NOWĄ GRĘ DO DANYCH MODELU
        this.view.addGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent addGame) {
                removeRowFilter();                                                                                  //-> Usuwa filtrowanie
                view.newGameDialog();                                                                               //-> Włącza widok kreatora tworzenia nowej gry
                view.createNewGameObject.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(checkAllFields()) {                                                                      //-> Weryfikuje poprawność wszystkich pól
                            String title = view.textFieldTitle.getText();
                            String producer = view.textFieldProducer.getText();
                            String category = view.textFieldCategory.getText();
                            Integer players = Integer.parseInt(view.textFieldNoOfPlayers.getValue().toString());
                            Float price = Float.parseFloat(view.textFieldPrice.getValue().toString());
                            model.addObject(new Game(title,producer,category,players,price));                       //-> Tworzy i dodaje obiekt nowej gry
                            view.textFieldTitle.setText(null);                                                      // |
                            view.textFieldProducer.setText(null);                                                   // |
                            view.textFieldCategory.setText(null);                                                   // |->  Resetuje pola w widoku kreatora nowej gry
                            view.textFieldNoOfPlayers.setValue(null);                                               // |
                            view.textFieldPrice.setValue(null);                                                     // |
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(),"Uzupełnij wszystkie pola !");
                        }
                    }
                });
                view.saveGamesButton.setEnabled(true);                       // |
                view.removeGameButton.setEnabled(true);                      // |-> Aktywuje / Dezaktywuje przyciski
                view.filterButton.setEnabled(true);                          // |
            }
        });

        //    USUŃ ZAZNACZONE GRY Z DANYCH MODELU (OBIEKTY [Game] Z FLAGĄ [false] w kolumnie numer: 0)
        this.view.removeGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removeRowFilter();                                                                                  //-> Usuwa filtrowanie
                model.removeFromTableData();                                                                        //-> Usuwa obiekty z flagą [false]
                if(model.getRowCount()==0){
                    view.removeGameButton.setEnabled(false);                // |
                    view.saveGamesButton.setEnabled(false);                 // |-> Aktywuje / Dezaktywuje przyciski
                    view.filterButton.setEnabled(false);                    // |
                    view.unfilterButton.setEnabled(false);                  // |
                }
            }
        });

        //    FILTRUJ DANE MODELU W OPARCIU O AKTUALNĄ LISTĘ FILTRÓW [MyRowFilter()]
        this.view.filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (MyRowFilter().isEmpty()) {
                    removeRowFilter();
                } else {
                    model.sorter.setRowFilter(RowFilter.andFilter(MyRowFilter()));        //-> Ustawia filtrowanie danych modelu
                    view.unfilterButton.setEnabled(true);                                 //-> Aktywuje przycisk
                }
            }
        });

        //    USUWA FILTROWANIE DANYCH MODELU
        this.view.unfilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removeRowFilter();                                                       //-> Usuwa filtrowanie danych modelu
                view.filterPriceFROM.setValue(null);
                view.filterPriceTO.setValue(null);
                view.filterButton.setEnabled(true);                                      //-> Aktywuje przycisk
            }
        });

        //    ZAPISUJE DANE MODELU [saveTableModel(String saveToFilePath)]
        this.view.saveGamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.selectFileDialog(actionEvent);
                try {
                    removeRowFilter();                                                                          //-> Usuwa filtrowanie danych modelu
                    String saveFilePath = view.saveFileChooser.getSelectedFile().getAbsolutePath();             //-> Pobiera ścieżkę do zapisku danych modelu
                    saveTableModel(saveFilePath);                                                               //-> Zapisuje dane modelu do pliku
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(new JFrame(),"Nie wskazano poprawnej lokalizacji !");
                }

            }
        });

        //    WCZYTUJE DANE MODELU [readTableModel(String loadFromFilePath)]
        this.view.loadGamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.selectFileDialog(actionEvent);
                try {
                    removeRowFilter();                                                                          //-> Usuwa filtrowanie danych modelu
                    String loadFilePath = view.loadFileChooser.getSelectedFile().getAbsoluteFile().toString();  //-> Pobiera ścieżkę do wczytania danych modelu
                    readTableModel(loadFilePath);                                                               //-> Wczytuje dane modelu z pliku .txt
                    view.saveGamesButton.setEnabled(true);                                                      // |
                    view.removeGameButton.setEnabled(true);                                                     // |-> Aktywuje przyciski
                    view.filterButton.setEnabled(true);                                                         // |
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(new JFrame(),"Nie wybrano poprawnego pliku !");
                }
            }
        });
        this.view.removeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
            }
        });
    }

    //    ------------------------------------
    //    ZAPISUJE DANE MODELU [DataVector] DO PLIKU TXT
    //    ------------------------------------

    public void saveTableModel(String saveToFilePath){
        String fileName = "JavaObjectFile_"+ System.currentTimeMillis() + "_.txt";                                 //-> Tworzy nazwę pliku .txt z stemplem czasowym
        ObjectOutputStream bufferedWriter = null;
        try {
            bufferedWriter = new ObjectOutputStream(new FileOutputStream(saveToFilePath + "\\" + fileName));
            bufferedWriter.writeObject((Vector) this.model.getDataVector());                                       //-> Zapisuje dane Vektora modelu danych do pliku .txt
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        } {
            if(bufferedWriter!=null) {
                try {
                    bufferedWriter.close();
                } catch (Exception ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
        }
    }

    //    ------------------------------------
    //    WCZYTUJE DANE MODELU [DataVector] Z PLIKU TXT
    //    ------------------------------------

    public void readTableModel(String loadFromFilePath){
        ObjectInputStream bufferedReader = null;
        try {
            bufferedReader = new ObjectInputStream(new FileInputStream(loadFromFilePath));
            this.model.setRowCount(0);                                                                   //-> Usuwa wszystkie dane z modelu
            Vector vector = (Vector)  bufferedReader.readObject();                                       //-> Wczytuje dane Vektora modelu danych z pliku .txt
            for (Object aVector : vector) {                                                              //-> Iteruje po obiektach z pliku .txt i dodaje do modelu danych
                this.model.addRow((Vector) aVector);
            }

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            if (bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
        }
    }
}
