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
            this.setLocationRelativeTo(null);
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
     
     DB_AddEntry addScreen = new DB_AddEntry();
     addScreen.setVisible(false);
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
        btnNextPage = new javax.swing.JButton();
        btnPrevPage = new javax.swing.JButton();
        btnViewEntry = new javax.swing.JButton();
        btnDelEntry = new javax.swing.JButton();
        btnAddEntry = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnResetSearch = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));
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

        btnNextPage.setBackground(new java.awt.Color(54, 79, 107));
        btnNextPage.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNextPage.setForeground(new java.awt.Color(255, 255, 255));
        btnNextPage.setText("Next Page");
        btnNextPage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        btnPrevPage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPrevPage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevPageActionPerformed(evt);
            }
        });

        btnViewEntry.setBackground(new java.awt.Color(54, 79, 107));
        btnViewEntry.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnViewEntry.setForeground(new java.awt.Color(255, 255, 255));
        btnViewEntry.setText("VIEW/EDIT ENTRY");
        btnViewEntry.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnViewEntry.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnViewEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnViewEntryMouseClicked(evt);
            }
        });
        btnViewEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewEntryActionPerformed(evt);
            }
        });

        btnDelEntry.setBackground(new java.awt.Color(252, 81, 133));
        btnDelEntry.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnDelEntry.setForeground(new java.awt.Color(255, 255, 255));
        btnDelEntry.setText("REMOVE ENTRY");
        btnDelEntry.setToolTipText("");
        btnDelEntry.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
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

        btnAddEntry.setBackground(new java.awt.Color(153, 255, 153));
        btnAddEntry.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnAddEntry.setText("ADD ENTRY");
        btnAddEntry.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAddEntry.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddEntryMouseClicked(evt);
            }
        });
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
        btnResetSearch.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnResetSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetSearchMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(2, 113, 121));
        jPanel1.setMaximumSize(new java.awt.Dimension(1300, 90));
        jPanel1.setMinimumSize(new java.awt.Dimension(1300, 90));
        jPanel1.setName(""); // NOI18N

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Pamantasan ng Lungsod ng Maynila");

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("PDS Management System");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/PLM-Seal.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResetSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(469, 469, 469)
                        .addComponent(btnPrevPage)
                        .addGap(18, 18, 18)
                        .addComponent(btnNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnViewEntry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1291, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                        .addComponent(btnViewEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPrevPage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnResetSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
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

    private void btnAddEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddEntryMouseClicked
        // TODO add your handling code here:
        DB_AddEntry addScreen = new DB_AddEntry();
        
        addScreen.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAddEntryMouseClicked

    private void btnViewEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewEntryMouseClicked
        // TODO add your handling code here:
        int row = tblEntries.getSelectedRow();
        
        if (row == -1){
            JOptionPane.showMessageDialog(this, "Please select a row to view/edit.");
        }
        else{
        DB_ViewUpdate updScreen = new DB_ViewUpdate();
        
        
        try {
            
        conn = connection.connect();
        
        PreparedStatement psPersonalInfo = null;
        ResultSet rsPersonalInfo = null;
        
        String sqlPersonalInfo = "SELECT * FROM infoman_pds.personal_info WHERE p_id = '"+tblEntries.getModel().getValueAt(row, 0).toString()+"'";
        psPersonalInfo = conn.prepareStatement(sqlPersonalInfo);
        rsPersonalInfo = psPersonalInfo.executeQuery();
                
        // statements for combo boxes
        
        // Personal Information 
        while(rsPersonalInfo.next()) {
            updScreen.txtL_name.setText(rsPersonalInfo.getString("l_name"));
            updScreen.txtF_name.setText(rsPersonalInfo.getString("f_name"));
            updScreen.txtM_name.setText(rsPersonalInfo.getString("m_name"));
            // updScreen.txtName_ext.setText(rsPersonalInfo.getString("name_ext"));
            updScreen.dateChooser.setDate(rsPersonalInfo.getDate("dob"));
            updScreen.txtPob.setText(rsPersonalInfo.getString("pob"));
            updScreen.txtHeight.setText(rsPersonalInfo.getString("height"));
            updScreen.txtWeight.setText(rsPersonalInfo.getString("weight"));
            updScreen.txtBlood_type.setText(rsPersonalInfo.getString("blood_type"));
            updScreen.txtHeight.setText(rsPersonalInfo.getString("height"));
            updScreen.txtGSIS_no.setText(rsPersonalInfo.getString("gsis_no"));
            updScreen.txtPAGIBIG_no.setText(rsPersonalInfo.getString("pagibig_id"));
            updScreen.txtPHILHEALTH_no.setText(rsPersonalInfo.getString("philhealth_id"));
            updScreen.txtSSS_no.setText(rsPersonalInfo.getString("sss_no"));
            updScreen.txtTIN_no.setText(rsPersonalInfo.getString("tin"));
            updScreen.txtAgency_no.setText(rsPersonalInfo.getString("agency_empno"));
            
            // combo boxes
            String sqlComboBoxSex = "SELECT sex_desc FROM ref_sex WHERE sex_id = '"+ rsPersonalInfo.getInt("sex_id") +"'";
            String sqlComboBoxCStat = "SELECT cstat_desc FROM ref_civilstatus WHERE cstat_id = '"+ rsPersonalInfo.getInt("cstat_id") +"'";
            String sqlComboBoxCit = "SELECT cit_desc FROM ref_citizenship WHERE cit_id = '"+ rsPersonalInfo.getInt("cit_id") +"'";
            String sqlComboBoxCitAcq = "SELECT cit_acq_desc FROM ref_cit_acq WHERE cit_acq_id = '"+ rsPersonalInfo.getInt("cit_acq_id") +"'";
            
            PreparedStatement psComboBoxSex = conn.prepareStatement(sqlComboBoxSex);
            PreparedStatement psComboBoxCStat = conn.prepareStatement(sqlComboBoxCStat);
            PreparedStatement psComboBoxCit = conn.prepareStatement(sqlComboBoxCit);
            PreparedStatement psComboBoxCitAcq = conn.prepareStatement(sqlComboBoxCitAcq);
            
            ResultSet rsComboBoxSex = psComboBoxSex.executeQuery();
            ResultSet rsComboBoxCStat = psComboBoxCStat.executeQuery();
            ResultSet rsComboBoxCit = psComboBoxCit.executeQuery();
            ResultSet rsComboBoxCitAcq = psComboBoxCitAcq.executeQuery();
            
            while(rsComboBoxSex.next())
                updScreen.txtSex.setSelectedItem((rsComboBoxSex.getString("sex_desc")).toString());
            while(rsComboBoxCStat.next())
                updScreen.txtSex.setSelectedItem((rsComboBoxCStat.getString("cstat_desc")).toString());
            while(rsComboBoxCit.next())
                updScreen.txtSex.setSelectedItem((rsComboBoxCit.getString("cit_desc")).toString());
            while(rsComboBoxCitAcq.next())
                updScreen.txtSex.setSelectedItem((rsComboBoxCitAcq.getString("cit_acq_desc")).toString());
        
        // Contact Information
        PreparedStatement psContactInfo = null;
        ResultSet rsContactInfo = null;
        
        String sqlContactInfo = "SELECT * FROM infoman_pds.contact_info WHERE p_id = '"+tblEntries.getModel().getValueAt(row, 0).toString()+"'";
        psContactInfo = conn.prepareStatement(sqlContactInfo);
        rsContactInfo = psContactInfo.executeQuery();
        
        while (rsContactInfo.next()) {
            updScreen.txtResHouse_no.setText(rsContactInfo.getString("res_house_no"));
            updScreen.txtResHouse_street.setText(rsContactInfo.getString("res_house_street"));
            updScreen.txtResHouse_village.setText(rsContactInfo.getString("res_village"));
            updScreen.txtResHouse_bgy.setText(rsContactInfo.getString("res_bgy"));
            updScreen.txtResHouse_citymun.setText(rsContactInfo.getString("res_citymun"));
            updScreen.txtResHouse_prov.setText(rsContactInfo.getString("res_prov"));
            updScreen.txtResHouse_zipcode.setText(rsContactInfo.getString("res_zipcode"));
            
            updScreen.txtPermHouse_no.setText(rsContactInfo.getString("perm_house_no"));
            updScreen.txtPermHouse_street.setText(rsContactInfo.getString("perm_house_street"));
            updScreen.txtPermHouse_village.setText(rsContactInfo.getString("perm_village"));
            updScreen.txtPermHouse_brgy.setText(rsContactInfo.getString("perm_bgy"));
            updScreen.txtPermHouse_citymun.setText(rsContactInfo.getString("perm_citymun"));
            updScreen.txtPermHouse_prov.setText(rsContactInfo.getString("perm_prov"));
            updScreen.txtPermHouse_zipcode.setText(rsContactInfo.getString("perm_zipcode"));
        }
            
            
        // Family Background
        PreparedStatement psFamilyBackground = null;
        ResultSet rsFamilyBackground = null;
        
        String sqlFamilyBackground = "SELECT * FROM infoman_pds.family_background WHERE p_id = '"+tblEntries.getModel().getValueAt(row, 0).toString()+"'";
        psFamilyBackground = conn.prepareStatement(sqlFamilyBackground);
        rsFamilyBackground = psFamilyBackground.executeQuery();
        
        while (rsFamilyBackground.next()) {
            updScreen.txtSpouse_Lname.setText(rsFamilyBackground.getString("spouse_lname"));
            updScreen.txtSpouse_Mname.setText(rsFamilyBackground.getString("spouce_mname"));
            updScreen.txtSpouse_Fname.setText(rsFamilyBackground.getString("spouse_fname"));
            updScreen.txtSpouse_occupation.setText(rsFamilyBackground.getString("spouce_occupation"));
            updScreen.txtSpouse_employer.setText(rsFamilyBackground.getString("spouce_employer"));
            updScreen.txtSpouse_bussAdd.setText(rsFamilyBackground.getString("spouce_emp_address"));
            
            updScreen.txtFather_Lname.setText(rsFamilyBackground.getString("father_lname"));
            updScreen.txtFather_Fname.setText(rsFamilyBackground.getString("father_fname"));
            updScreen.txtFather_Mname.setText(rsFamilyBackground.getString("father_mname"));
            updScreen.txtFather_nameExt.setText(rsFamilyBackground.getString("father_name_ext"));
            
            updScreen.txtMother_Lname.setText(rsFamilyBackground.getString("mother_mn_lname"));
            updScreen.txtMother_Fname.setText(rsFamilyBackground.getString("mother_mn_fname"));
            updScreen.txtMother_Mname.setText(rsFamilyBackground.getString("mother_mn_mname"));
        }
        
        }
        
        
        
        } catch (Exception e) {
            System.out.println(e);
        }
        updScreen.setVisible(true);
        this.dispose();
        }
    }//GEN-LAST:event_btnViewEntryMouseClicked

    private void btnViewEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewEntryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewEntryActionPerformed

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
    public javax.swing.JButton btnAddEntry;
    public javax.swing.JButton btnDelEntry;
    public javax.swing.JButton btnNextPage;
    public javax.swing.JButton btnPrevPage;
    public javax.swing.JButton btnResetSearch;
    public javax.swing.JButton btnViewEntry;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tblEntries;
    public javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private PreparedStatement prepareStatement(String sqlPersonalInfo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
