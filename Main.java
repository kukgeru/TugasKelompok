import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Main {

    // Kelas untuk menyimpan data film
    static class Film {
        String judul;
        double harga;
        String posterPath;
        List<String> kursiTersedia;

        Film(String judul, double harga, String posterPath, List<String> kursiTersedia) {
            this.judul = judul;
            this.harga = harga;
            this.posterPath = posterPath;
            this.kursiTersedia = kursiTersedia;
        }
    }

    // Kelas untuk menyimpan data pesanan
    static class Pesanan {
        String judulFilm;
        List<String> kursi;
        String namaPemesan;

        Pesanan(String judulFilm, List<String> kursi, String namaPemesan) {
            this.judulFilm = judulFilm;
            this.kursi = kursi;
            this.namaPemesan = namaPemesan;
        }

        @Override
        public String toString() {
            return "Nama: " + namaPemesan + ", Film: " + judulFilm + ", Kursi: " + String.join(", ", kursi);
        }

        public double getTotalHarga(double hargaPerKursi) {
            return hargaPerKursi * kursi.size();
        }
    }

    private List<Film> daftarFilm = new ArrayList<>();
    private List<Pesanan> daftarPesanan = new ArrayList<>();
    private DefaultListModel<String> pesananListModel = new DefaultListModel<>();
    private JLabel totalHargaLabel;

    public Main() {
        // Menambahkan daftar film
        daftarFilm.add(new Film("Avatar: The Way of Water", 50000, "https://upload.wikimedia.org/wikipedia/id/5/54/Avatar_The_Way_of_Water_poster.jpg", generateKursi("A")));
        daftarFilm.add(new Film("Spider-Man: No Way Home", 45000, "https://upload.wikimedia.org/wikipedia/id/2/20/Poster_Spider-Man_No_Way_Home.jpg", generateKursi("B")));
        daftarFilm.add(new Film("The Batman", 60000, "https://upload.wikimedia.org/wikipedia/id/5/54/The_Batman_%28film%29_poster.jpeg", generateKursi("C")));

        // Membuat frame utama
        JFrame frame = new JFrame("Bioskop Kasir");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        // Menambahkan panel header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 45, 65));
        JLabel headerLabel = new JLabel("Bioskop Kasir");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Panel daftar film
        JPanel panelFilm = new JPanel();
        panelFilm.setLayout(new GridLayout(daftarFilm.size(), 1, 10, 10));
        panelFilm.setBackground(new Color(240, 240, 240));

        for (Film film : daftarFilm) {
            JPanel filmPanel = new JPanel();
            filmPanel.setLayout(new BorderLayout());
            filmPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            filmPanel.setBackground(Color.WHITE);

            JLabel labelJudul = new JLabel("<html><strong>" + film.judul + "</strong><br>Harga: Rp" + film.harga + "</html>");
            labelJudul.setFont(new Font("Arial", Font.PLAIN, 14));
            JButton buttonPilih = new JButton("Pilih");

            buttonPilih.setBackground(new Color(0, 123, 255));
            buttonPilih.setForeground(Color.WHITE);
            buttonPilih.setFocusPainted(false);

            buttonPilih.addActionListener(e -> pilihFilm(film));

            // Menampilkan poster film dari URL
            JLabel labelPoster = new JLabel();
            try {
                URL url = new URL(film.posterPath);
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                labelPoster.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                labelPoster.setText("Poster tidak tersedia");
                labelPoster.setHorizontalAlignment(SwingConstants.CENTER);
            }

            filmPanel.add(labelPoster, BorderLayout.WEST);
            filmPanel.add(labelJudul, BorderLayout.CENTER);
            filmPanel.add(buttonPilih, BorderLayout.EAST);

            panelFilm.add(filmPanel);
        }

        JScrollPane scrollPaneFilm = new JScrollPane(panelFilm);
        scrollPaneFilm.setBorder(BorderFactory.createEmptyBorder());

        // Panel pesanan
        JPanel panelPesanan = new JPanel();
        panelPesanan.setLayout(new BorderLayout());
        panelPesanan.setBackground(new Color(240, 240, 240));
        panelPesanan.setBorder(BorderFactory.createTitledBorder("Pesanan"));

        JLabel labelPesanan = new JLabel("Daftar Pesanan:");
        labelPesanan.setFont(new Font("Arial", Font.BOLD, 16));

        JList<String> listPesanan = new JList<>(pesananListModel);
        listPesanan.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton buttonHapusPesanan = new JButton("Hapus Pesanan");
        buttonHapusPesanan.setBackground(new Color(220, 53, 69));
        buttonHapusPesanan.setForeground(Color.WHITE);
        buttonHapusPesanan.setFocusPainted(false);
        buttonHapusPesanan.addActionListener(e -> hapusPesanan(listPesanan));

        JButton buttonUpdatePesanan = new JButton("Update Pesanan");
        buttonUpdatePesanan.setBackground(new Color(255, 193, 7));
        buttonUpdatePesanan.setForeground(Color.BLACK);
        buttonUpdatePesanan.setFocusPainted(false);
        buttonUpdatePesanan.addActionListener(e -> updatePesanan(listPesanan));

        JPanel panelTombolPesanan = new JPanel();
        panelTombolPesanan.setLayout(new GridLayout(2, 1, 10, 10)); // Mengatur tombol menjadi vertikal
        panelTombolPesanan.add(buttonHapusPesanan);
        panelTombolPesanan.add(buttonUpdatePesanan);

        JPanel panelFooterPesanan = new JPanel();
        panelFooterPesanan.setLayout(new BorderLayout());
        panelFooterPesanan.add(panelTombolPesanan, BorderLayout.NORTH);
        totalHargaLabel = new JLabel("Total: Rp 0");
        totalHargaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelFooterPesanan.add(totalHargaLabel, BorderLayout.SOUTH);

        panelPesanan.add(labelPesanan, BorderLayout.NORTH);
        panelPesanan.add(new JScrollPane(listPesanan), BorderLayout.CENTER);
        panelPesanan.add(panelFooterPesanan, BorderLayout.SOUTH);

        // Menambahkan ke frame utama
        frame.add(scrollPaneFilm, BorderLayout.CENTER);
        frame.add(panelPesanan, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void hapusPesanan(JList<String> listPesanan) {
        int selectedIndex = listPesanan.getSelectedIndex();
        if (selectedIndex != -1) {
            Pesanan pesanan = daftarPesanan.get(selectedIndex);
            for (Film film : daftarFilm) {
                if (film.judul.equals(pesanan.judulFilm)) {
                    film.kursiTersedia.addAll(pesanan.kursi);
                    break;
                }
            }
            daftarPesanan.remove(selectedIndex);
            pesananListModel.remove(selectedIndex);
            updateTotalHarga();
        }
    }

    private void updatePesanan(JList<String> listPesanan) {
        int selectedIndex = listPesanan.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Pilih pesanan yang ingin diupdate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pesanan pesanan = daftarPesanan.get(selectedIndex);
        for (Film film : daftarFilm) {
            if (film.judul.equals(pesanan.judulFilm)) {
                // Kembalikan kursi dari pesanan ke daftar kursi tersedia
                film.kursiTersedia.addAll(pesanan.kursi);

                // Konfirmasi update
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Apakah Anda ingin memilih ulang film untuk pesanan ini?",
                        "Konfirmasi Update",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    pilihFilm(film);
                    daftarPesanan.remove(selectedIndex);
                    pesananListModel.remove(selectedIndex);
                }
                break;
            }
        }
    }

    private void pilihFilm(Film film) {
        JFrame kursiFrame = new JFrame("Pilih Kursi - " + film.judul);
        kursiFrame.setSize(600, 400);
        kursiFrame.setLayout(new BorderLayout());

        JPanel panelKursi = new JPanel();
        panelKursi.setLayout(new GridLayout(10, 10, 5, 5));

        Map<JButton, String> tombolKursiMap = new HashMap<>();
        List<String> kursiDipilih = new ArrayList<>();

        for (String kursi : film.kursiTersedia) {
            JButton tombolKursi = new JButton(kursi);
            tombolKursi.setBackground(Color.GREEN);
            tombolKursi.setFocusPainted(false);
            tombolKursiMap.put(tombolKursi, kursi);

            tombolKursi.addActionListener(e -> {
                String kursiDipilihString = tombolKursiMap.get(tombolKursi);
                if (!kursiDipilih.contains(kursiDipilihString)) {
                    kursiDipilih.add(kursiDipilihString);
                    tombolKursi.setBackground(Color.RED);
                    tombolKursi.setEnabled(false);
                } else {
                    kursiDipilih.remove(kursiDipilihString);
                    tombolKursi.setBackground(Color.GREEN);
                    tombolKursi.setEnabled(true);
                }
            });

            panelKursi.add(tombolKursi);
        }

        JButton konfirmasiButton = new JButton("Konfirmasi Pemesanan");
        konfirmasiButton.addActionListener(e -> {
            if (kursiDipilih.isEmpty()) {
                JOptionPane.showMessageDialog(kursiFrame, "Pilih minimal satu kursi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String namaPemesan = JOptionPane.showInputDialog(kursiFrame, "Masukkan Nama Pemesan:");
            if (namaPemesan != null && !namaPemesan.isEmpty()) {
                Pesanan pesanan = new Pesanan(film.judul, kursiDipilih, namaPemesan);
                daftarPesanan.add(pesanan);
                pesananListModel.addElement(pesanan.toString());

                for (String kursi : kursiDipilih) {
                    film.kursiTersedia.remove(kursi);
                }

                updateTotalHarga();
                kursiFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(kursiFrame, "Nama pemesan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        kursiFrame.add(panelKursi, BorderLayout.CENTER);
        kursiFrame.add(konfirmasiButton, BorderLayout.SOUTH);
        kursiFrame.setVisible(true);
    }

    private void updateTotalHarga() {
        double totalHarga = 0;
        for (Pesanan pesanan : daftarPesanan) {
            for (Film film : daftarFilm) {
                if (film.judul.equals(pesanan.judulFilm)) {
                    totalHarga += pesanan.getTotalHarga(film.harga);
                    break;
                }
            }
        }
        totalHargaLabel.setText("Total: Rp " + totalHarga);
    }

    private List<String> generateKursi(String prefix) {
        List<String> kursi = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            kursi.add(prefix + i);
        }
        return kursi;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}