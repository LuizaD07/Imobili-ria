package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Contrato;

public class ContratoDAO {
    private Connection bd;

    public ContratoDAO() {
        this.bd = BancoDeDados.getBd();
    }

    public void create(Contrato contrato) throws SQLException {
                                                    //NAO PRECISA DO "NULL"?                                            
        String query = "INSERT INTO contrato (id_cliente, id_corretor, id_imovel, data_inicio, data_fim, comissao) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement st = this.bd.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        st.setInt(1, contrato.getId_cliente());
        st.setInt(2, contrato.getId_corretor());
        st.setInt(3, contrato.getId_imovel());
        st.setString(4, contrato.getData_inicio());
        st.setString(5, contrato.getData_fim());
        st.setString(6, contrato.getComissao());
        st.executeUpdate(); 

        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int idGerado = generatedKeys.getInt(1); // Obtem o ID gerado
                contrato.setId_contrato(idGerado); // 
            } else {
                throw new SQLException("Falha ao obter o ID gerado.");
            }
        }
    }

    //UPDATE
    public void update (Contrato con) throws SQLException {
        String query = """
        UPDATE FROM contrato
        SET data_inicio = ?, data_fim = ?
        WHERE  id_imovel = ?
        """;
        PreparedStatement st = this.bd.prepareStatement(query);
        st.setString(con.getData_inicio());
        st.setString(con.getData_fim());
        st.setInt(con.getId_imovel());
        st.executeUpdate();
    }

    //CONSULTA
    ArrayList<Contrato>findByNomeLike(String n) throws SQLException {
        ArrayList<Contrato> lista = new ArrayList<Contrato>();
        String query = """
        SELECT FROM contrato
        WHERE  data_inicio LIKE ?
        """;
        PreparedStatement st = this.bd.prepareStatement(query);
        st.setString(1, "%" + n + "%");
        ResultSet res = st.executeQuery();
        while(res.next()) {
            int id = res.getInt("id");
            int id_cliente = res.getInt("id_cliente");
            int id_corretor = res.getInt("id_corretor");
            int id_imovel = res.getInt("id_imovel");
            String data_inicio = res.getData_inicio("data_inicio");
            String data_fim = res.getData_fim("data_fim");  
            Contrato cont = new Contrato(id, id_cliente, id_corretor, id_imovel, data_inicio, data_fim, comissao);
            lista.add(cont); 
        }
        return lista;
    }

    ///DELETE
    public void delete(Contrato con) throws SQLException {
        String query = """
        DELETE FROM contrato
        WHERE id_cliente = ?
        """;
    }

    

    public ArrayList<Contrato> getAll() throws SQLException {
        ArrayList<Contrato> listaContratos = new ArrayList<>();
        String query = "SELECT id_contrato, id_cliente, id_corretor, id_imovel, data_inicio, data_fim, comissao FROM contrato";
        PreparedStatement st = this.bd.prepareStatement(query);
        ResultSet res = st.executeQuery();
        while (res.next()) {
            int id_contrato = res.getInt("id");
            int id_cliente = res.getInt("id_cliente");
            int id_corretor = res.getInt("id_corretor");
            int id_imovel = res.getInt("id_imovel");
            String data_inicio = res.getString("data_inicio");
            String data_fim = res.getString("data_fim");
            String comissao = res.getString("comissao");
            Contrato contrato = new Contrato(id_contrato, id_cliente, id_corretor, id_imovel, data_inicio, data_fim, comissao);
            listaContratos.add(contrato);
        }
        return listaContratos;
    }

} /