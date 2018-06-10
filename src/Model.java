import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * @author Marcin Rudko
 * @version 31.05.2018
 **/


public class Model extends DefaultTableModel {
    private String [] tableHeaders = {"Wybór","Producent","Tytuł","Gatunek","Liczba graczy","Cena"};
    private Object [] tableClases = {true,String.class,String.class,String.class,int.class,Double.class};
    protected TableRowSorter<Model> sorter = new TableRowSorter<>(this);


    //    ------------------------------------
    //    DODAJ OBIEKT TYPU -> [Game]
    //    ------------------------------------

    public void addObject(Game game){
        this.addRow(game.game);
        this.fireTableDataChanged();
    }

    //    ------------------------------------
    //    USUŃ OBIEKTY Z FLAGĄ -> [true]
    //    ------------------------------------

    public void removeFromTableData() {
        if(!this.dataVector.isEmpty()) {
            int rows = this.getRowCount();
            for (int i = rows; i > 0; i--) {
                if (this.getValueAt(i-1, 0).equals(true)) {
                    this.setValueAt(false, i-1, 0);
                    this.removeRow(i-1);
                }
            }
        }
        this.fireTableDataChanged();
    }

    //    ------------------------------------
    //    POBIERZ ODPOWIEDNIĄ KLASĘ KOLUMN
    //    ------------------------------------

    @Override
    public Class<?> getColumnClass(int column) {
        if(this.dataVector.isEmpty()) return tableClases[column].getClass();
        else return this.getValueAt(0,column).getClass();
    }

    //    ------------------------------------
    //    USTAW MOŻLIWOŚĆ EDYCJI KOLUMNY -> [Wybór]
    //    ------------------------------------

    @Override
    public boolean isCellEditable(int row, int column){
        if(column==0) return true;
        else return false;
    }

    //    ------------------------------------
    //    POBIERZ NAGŁÓWKI DO TABELI
    //    ------------------------------------

    @Override
    public String getColumnName(int x){
        return tableHeaders[x];
    }

    //    ------------------------------------
    //    POBIERZ ILOŚĆ ELEMENTÓW TABELI
    //    ------------------------------------

    @Override
    public int getRowCount() {
        return this.dataVector.size();
    }

    //    ------------------------------------
    //    POBIERZ ILOŚĆ KOLUMN TABELI
    //    ------------------------------------

    @Override
    public int getColumnCount() {
        return tableHeaders.length;
    }

}
