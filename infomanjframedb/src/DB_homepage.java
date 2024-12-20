import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.SwingConstants;
import net.proteanit.sql.DbUtils;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;

public class DB_homepage extends javax.swing.JFrame {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int currentPage = 0;  // Track current page (0-based index)
    private int totalRows = 0;    // Track total number of rows
    
    public void refresh(){
            try {
            conn = connection.connect();
            
            // Get total rows for pagination (this query counts all the rows)
            String countQuery = "SELECT COUNT(*) FROM personal_info";
            ps = conn.prepareStatement(countQuery);
            rs = ps.executeQuery();
            if (rs.next()) {
                totalRows = rs.getInt(1);
            }
            
            // Fetch 10 rows based on the current page
            String query = "SELECT p.p_id AS 'ID', CONCAT(p.l_name, ', ', p.f_name, ' ', p.m_name) AS 'Full Name', " +
                           "s.sex_desc AS 'Gender', p.dob AS 'Birthday', c.cstat_desc AS 'Civil Status' " +
                           "FROM personal_info p " +
                           "JOIN ref_sex s ON p.sex_id = s.sex_id " +
                           "JOIN ref_civilstatus c ON p.cstat_id = c.cstat_id " +
                           "ORDER BY p.p_id " +
                           "LIMIT 40 OFFSET ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, currentPage * 10); // Offset based on the current page
            rs = ps.executeQuery();
            
            // Set the result set to the table model
            tblEntries.setModel(DbUtils.resultSetToTableModel(rs));
            
            // Customize the table header
            JTableHeader tblHeader = tblEntries.getTableHeader();
            DefaultTableCellRenderer tblRenderer = (DefaultTableCellRenderer) tblHeader.getDefaultRenderer();
            tblRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Sets horizontal alignment
            tblHeader.setBackground(new Color(54, 79, 107));  // Sets background color
            tblHeader.setForeground(Color.WHITE);  // Sets font color
            tblHeader.setFont(new Font("Century Gothic", Font.BOLD, 12));  // Sets font family and size
            
        } catch (Exception e) {
            e.printStackTrace();  // Better error handling
        }
    }
    private void deleteEntry(int p_id) {
    try {
        conn = connection.connect();  // Establish a connection to the database

        // Step 2: Delete the entry from the personal_info table
        String deletePersonalQuery = "DELETE FROM personal_info WHERE p_id = ?";
        ps = conn.prepareStatement(deletePersonalQuery);
        ps.setInt(1, p_id);
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Entry deleted successfully.");
        
        // Refresh the table data after deletion
        refresh();  // This method reloads the data into the table
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error deleting the entry: " + e.getMessage());
    }
}
    private void searchEntries() {
    String searchTerm = txtSearch.getText().trim();

        String query = "SELECT p.p_id AS 'ID', CONCAT(p.l_name, ', ', p.f_name, ' ', p.m_name) AS 'Full Name', " +
                       "s.sex_desc AS 'Gender', p.dob AS 'Birthday', c.cstat_desc AS 'Civil Status' " +
                       "FROM personal_info p " +
                       "JOIN ref_sex s ON p.sex_id = s.sex_id " +
                       "JOIN ref_civilstatus c ON p.cstat_id = c.cstat_id " +
                       (searchTerm.isEmpty() ? "" : "WHERE CONCAT(p.l_name, ' ', p.f_name, ' ', p.m_name) LIKE ? OR p.p_id LIKE ?") +
                       "ORDER BY p.p_id";

        try {
            conn = connection.connect();
            ps = conn.prepareStatement(query);

            if (!searchTerm.isEmpty()) {
                ps.setString(1, "%" + searchTerm + "%"); // Partial match for names
                ps.setString(2, "%" + searchTerm + "%"); // Partial match for IDs
            }

            rs = ps.executeQuery();
            tblEntries.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for entries: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public DB_homepage() {
     initComponents();
     this.setLocationRelativeTo(null);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntries = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnNextPage = new javax.swing.JButton();
        btnPrevPage = new javax.swing.JButton();
        btnViewEntry = new javax.swing.JButton();
        btnDelEntry = new javax.swing.JButton();
        btnAddEntry = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnResetSearch = new javax.swing.JButton();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tblEntries.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblEntries);

        jLabel1.setFont(new java.awt.Font("Gotham Bold", 0, 24)); // NOI18N
        jLabel1.setText("PDS MANAGEMENT SYSTEM");

        btnNextPage.setBackground(new java.awt.Color(54, 79, 107));
        btnNextPage.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNextPage.setForeground(new java.awt.Color(255, 255, 255));
        btnNextPage.setText("Next Page");
        btnNextPage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextPageActionPerformed(evt);
            }
        });

        btnPrevPage.setBackground(new java.awt.Color(54, 79, 107));
        btnPrevPage.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnPrevPage.setForeground(new java.awt.Color(255, 255, 255));
        btnPrevPage.setText("Previous Page");
        btnPrevPage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevPageActionPerformed(evt);
            }
        });

        btnViewEntry.setBackground(new java.awt.Color(54, 79, 107));
        btnViewEntry.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnViewEntry.setForeground(new java.awt.Color(255, 255, 255));
        btnViewEntry.setText("VIEW ENTRY");
        btnViewEntry.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnDelEntry.setBackground(new java.awt.Color(252, 81, 133));
        btnDelEntry.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnDelEntry.setForeground(new java.awt.Color(255, 255, 255));
        btnDelEntry.setText("REMOVE ENTRY");
        btnDelEntry.setToolTipText("");
        btnDelEntry.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        btnDelEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDelEntryMouseClicked(evt);
            }
        });
        btnDelEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelEntryActionPerformed(evt);
            }
        });

        btnAddEntry.setBackground(new java.awt.Color(54, 79, 107));
        btnAddEntry.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnAddEntry.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEntry.setText("ADD ENTRY");
        btnAddEntry.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEntryActionPerformed(evt);
            }
        });

        txtSearch.setToolTipText("input either ID or Name...");
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        btnResetSearch.setBackground(new java.awt.Color(54, 79, 107));
        btnResetSearch.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnResetSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnResetSearch.setText("CLEAR SEARCH");
        btnResetSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetSearchMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnResetSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPrevPage)
                                .addGap(18, 18, 18)
                                .addComponent(btnNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnViewEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(btnViewEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(191, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnPrevPage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnResetSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        refresh();
    }//GEN-LAST:event_formWindowActivated

    private void btnNextPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextPageActionPerformed
        if ((currentPage + 1) * 40 < totalRows) {
            currentPage++; // Move to the next page
            refresh();    // Reload the data for the next page
        }
    }//GEN-LAST:event_btnNextPageActionPerformed

    private void btnPrevPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevPageActionPerformed
       if (currentPage > 0) {
            currentPage--; // Move to the previous page
            refresh();    // Reload the data for the previous page
        }
    }//GEN-LAST:event_btnPrevPageActionPerformed
    
    private void btnDelEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelEntryActionPerformed
         int selectedRow = tblEntries.getSelectedRow();
    
    if (selectedRow != -1) {
        // Get the p_id of the selected row (assuming p_id is in the first column)
        int p_id = (int) tblEntries.getValueAt(selectedRow, 0);  // Column 0 is for ID
        
        // Ask for confirmation before deletion
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this entry?", 
                "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            deleteEntry(p_id);  // Call the method to delete the entry from the database
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a row to delete.");
    }
    }//GEN-LAST:event_btnDelEntryActionPerformed

    private void btnAddEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddEntryActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMouseClicked

    private void btnDelEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelEntryMouseClicked

    }//GEN-LAST:event_btnDelEntryMouseClicked

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        searchEntries();
    }//GEN-LAST:event_txtSearchKeyPressed

    private void btnResetSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetSearchMouseClicked
        txtSearch.setText("");  // Clear the search field
        refresh();  // Load all data back into the table
    }//GEN-LAST:event_btnResetSearchMouseClicked

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DB_homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DB_homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DB_homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DB_homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DB_homepage().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddEntry;
    private javax.swing.JButton btnDelEntry;
    private javax.swing.JButton btnNextPage;
    private javax.swing.JButton btnPrevPage;
    private javax.swing.JButton btnResetSearch;
    private javax.swing.JButton btnViewEntry;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEntries;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
