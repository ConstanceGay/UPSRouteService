package UPSRouteService;

public enum Location {
    _1CN_Emile_Durand ("1.466781,43.561202"),
    _1R1 ("1.466333,43.561258"),
    _1R2 ("1.466433,43.561711"),
    _1TP1 ("1.466882,43.561848"),
    _2A_Grignard_Le_Chatelier ("1.466588,43.563797"),
    _2TP1 ("1.467874,43.56453"),
    _2TP2 ("1.467251,43.564977"),
    _2TP3 ("1.467495,43.565103"),
    _3A ("1.469249,43.560172"),
    _3PN ("1.468367,43.560995"),
    _3TP1 ("1.468868,43.55984"),
    _3TP2 ("1.467792,43.560984"),
    _3R1 ("1.468336,43.559815"),
    _4R1 ("1.469319,43.558263"),
    _4R3 ("1.472296,43.559345"),
    _4TP4 ("1.471897,43.559304"),
    _4TP2 ("1.469702,43.558736"),
    AL_1_a_6 ("1.469207,43.560639"),
    Amphi_Fermat ("1.467068,43.562327"),
    Amphis_Langevin_et_Curie ("1.469473,43.559952"),
    Amphi_Leclerc_du_Sablon ("1.470326,43.558517"),
    Amphi_Molliard ("1.469902,43.558287"),
    Amphi_Stieltjes ("1.46732,43.562102"),
    BU_SCD ("1.465224,43.563641"),
    Espace_Reprographie ("1.467655,43.559999"),
    Espaces_Verts ("1.474605,43.558579"),
    Fablab ("1.468869,43.562642"),
    Forum_Louis_Larreng ("1.464054,43.561028"),
    Giordano_Bruno ("1.468669,43.564635"),
    Grand_Gymnase ("1.470327,43.56336"),
    Halle_technologique ("1.467762,43.557113"),
    IBCG ("1.471582,43.558619"),
    IRT_SaintExupery ("1.468074,43.565369"),
    Laplace_3R2 ("1.469438,43.561392"),
    Laplace_3R3 ("1.468678,43.56115"),
    Les_Integrales_A ("1.473731,43.558272"),
    Les_Integrales_B ("1.473342,43.558083"),
    Les_Integrales_C ("1.472955,43.557879"),
    Maison_de_la_Reussite_en_License ("1.46869,43.562615"),
    Marche_UniversiTerre ("1.464739,43.561759"),
    Parking_IUFM ("1.470656,43.556558"),
    Petit_Nouveau_Gymnase("1.470514,43.563072"),
    PMR ("1.471337,43.5627"),
    Residence_Clement_Ader ("1.466899,43.557067"),
    S_18 ("1.467872,43.565334"),
    S_17 ("1.467977,43.565383"),
    Sentier_Nature ("1.473999,43.559923"),
    Serre ("1.474794,43.558495"),
    Tripode_A ("1.462804,43.563059"),
    U1 ("1.470252,43.560316"),
    U2 ("1.470427,43.561324"),
    U3 ("1.469889,43.561998"),
    U4 ("1.469265,43.562685"),
    UFR_STAPS ("1.471832,43.561688"),
    GRAND_RU("1.463478,43.562038"),
    PETIT_RU("1.471717,43.560787"),
    IRIT("1.467919,43.561805"),
    BATIMENT_U3("1.469883,43.561883");

    private String coordinates;

    Location(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }
}
