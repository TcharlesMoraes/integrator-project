package services.facade;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectDataset implements Serializable {

    private List<String> campos;
    private List<String> tipos;
    private List<List<String>> consulta;
    private List<String> res;
    Map<String, List<String>> typeMap;

    public SelectDataset() {

    }

    public void findAll(String tableName) {
        consulta = new ArrayList<>();
        res = new ArrayList<>();
        String select = "SELECT * FROM " + tableName + ";";
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        try {
            conn = ConnexaoFactory.getConnection();
            ps = conn.prepareStatement(select,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = ps.executeQuery();
            if (rs.first()) {
                do {
                    int i = 0;
                    for (String campo : campos) {
                        res.add(rs.getString(campo));
                    }
                    consulta.add(res);
                    res = new ArrayList<>();
                } while (rs.next());
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getTypes(String tableName) {
        tipos = new ArrayList<>();
        typeMap = new HashMap<String, List<String>>();
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        try {
            conn = ConnexaoFactory.getConnection();
            ps = conn.prepareStatement("SELECT\n"
                    + "column_name,\n"
                    + "data_type\n"
                    + "FROM\n"
                    + "information_schema.columns\n"
                    + "WHERE table_name = '" + tableName + "';",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = ps.executeQuery();
            if (rs.first()) {
                do {
                    tipos.add(rs.getString("data_type"));
                    addTypeHash(rs.getString("data_type"), rs.getString("column_name"));
                } while (rs.next());
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public void executeQuery(String query, List<String> campos) {
        consulta = new ArrayList<>();
        res = new ArrayList<>();
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        try {
            conn = ConnexaoFactory.getConnection();
            ps = conn.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = ps.executeQuery();
            if (rs.first()) {
                do {
                    int i = 0;
                    for (String campo : campos) {
                        res.add(rs.getString(campo));
                    }
                    consulta.add(res);
                    res = new ArrayList<>();
                } while (rs.next());
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getColunas(String tableName) {
        campos = new ArrayList<>();
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        try {
            conn = ConnexaoFactory.getConnection();
            ps = conn.prepareStatement("SELECT\n"
                    + "column_name,\n"
                    + "data_type\n"
                    + "FROM\n"
                    + "information_schema.columns\n"
                    + "WHERE table_name = '" + tableName + "';",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = ps.executeQuery();
            if (rs.first()) {
                do {
                    campos.add(rs.getString("column_name"));
                } while (rs.next());
            }
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public List<String> getCampos() {
        return campos;
    }

    public void setCampos(List<String> campos) {
        this.campos = campos;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public List<List<String>> getConsulta() {
        return consulta;
    }

    public void setConsulta(List<List<String>> consulta) {
        this.consulta = consulta;
    }

    public Map<String, List<String>> getTypeMap() {
        return typeMap;
    }

    public void addTypeHash(String key, String value) {
        if (typeMap.get(key) != null) {
            typeMap.get(key).add(value);
        } else {
            typeMap.put(key, new ArrayList<String>(Arrays.asList(value)));
        }
    }

    public void setTypeMap(Map<String, List<String>> typeMap) {
        this.typeMap = typeMap;
    }
}
