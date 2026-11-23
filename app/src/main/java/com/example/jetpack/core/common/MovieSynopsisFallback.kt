package com.example.jetpack.core.common

object MovieSynopsisFallback {
    private val indonesianSynopses = mapOf(
        603692 to "John Wick mengungkap jalan untuk mengalahkan The High Table. Namun sebelum bisa meraih kebebasannya, Wick harus berhadapan dengan musuh baru dengan aliansi kuat di seluruh dunia dan kekuatan yang mengubah teman lama menjadi lawan.",
        94605 to "Dua saudara perempuan terikat dalam perselisihan sengit saat pertempuran antara kota kaya Piltover dan kota bawah Zaun yang kumuh memanas.",
        1396 to "Walter White, seorang guru kimia SMA yang didiagnosis menderita kanker paru-paru stadium akhir, mulai memproduksi metamfetamin bersama mantan muridnya untuk mengamankan masa depan keuangan keluarganya.",
        603 to "Neo, seorang peretas komputer, mempelajari kebenaran tentang realitas ilusi yang diciptakan oleh mesin cerdas dan bergabung dalam pemberontakan demi membebaskan manusia.",
        157336 to "Di masa depan ketika Bumi tidak lagi dapat dihuni, sekelompok penjelajah luar angkasa melakukan perjalanan melewati lubang cacing untuk mencari planet baru bagi umat manusia.",
        27205 to "Dom Cobb adalah pencuri terampil yang mencuri rahasia berharga dari alam bawah sadar selama mimpi. Dia ditawari kesempatan untuk menghapus catatan kriminalnya jika berhasil melakukan penanaman ide ke dalam pikiran target.",
        155 to "Batman harus menghadapi musuh paling berbahaya dan tak terduga, Joker, yang berniat menjerumuskan kota Gotham ke dalam anarki dan kekacauan total."
    )

    fun getIndonesianOverview(movieId: Int, currentOverview: String): String {
        if (currentOverview.isNotBlank()) return currentOverview
        return indonesianSynopses[movieId] ?: currentOverview
    }
}
