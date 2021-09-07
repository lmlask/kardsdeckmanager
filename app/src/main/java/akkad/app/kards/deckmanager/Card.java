package akkad.app.kards.deckmanager;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Card implements Comparable<Card>, Serializable {
    private static final String TAG = "Card.java";
    private String name;
    private String special;
    private String country;
    private String type;
    private String attackImage;
    private String rarity;
    private String id;

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion(String expansion) {
        this.expansion = expansion;
    }

    private String expansion;
    boolean passive;
    private int kredits, opCost, attack, defense;

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public int getRarityLimit() {
        HashMap<String, Integer> rarities = new HashMap<>();
        rarities.put("standard", 4);
        rarities.put("limited", 3);
        rarities.put("special", 2);
        rarities.put("elite", 1);

        int rarityLimit = rarities.get(this.rarity);

        try {
            return rarityLimit;
        } catch (NullPointerException ex) {
            Log.d(TAG, "getRarityLimit: NullPointerException on rarity limit on card " + this.getName());
            return 4;
        }
    }

    //Getters

    public boolean isPassive() {
        return passive;
    }

    public String getCountry() {
        //Maps string country to the correspondent color and returns that color as string
        Map<String, String> countryMap;
        countryMap = new HashMap<>();
        countryMap.put("japan", "#946B0F");
        countryMap.put("usa", "#4B521C");
        countryMap.put("britain", "#A29374");
        countryMap.put("soviet", "#43240B");
        countryMap.put("france", "#293659");
        countryMap.put("italy", "#5A554B");
        countryMap.put("germany", "#465142");
        try {
            return countryMap.get(country);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "getCountry: Wrong country in the cards file.");
            return "#FFFFFF";
        }
    }

    public String getCountryName() {
        return country;
    }

    public int getType() {
        Map<String, Integer> typeMap;
        typeMap = new HashMap<>();
        typeMap.put("infantry", R.drawable.infantry);
        typeMap.put("tank", R.drawable.tank);
        typeMap.put("artillery", R.drawable.artillery);
        typeMap.put("fighter", R.drawable.fighter);
        typeMap.put("bomber", R.drawable.bomber);
        typeMap.put("order", R.drawable.order);
        typeMap.put("countermeasure", R.drawable.cm);
        typeMap.put("hq", R.mipmap.ic_launcher);
        try {
            return typeMap.get(type);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "getType: NullPointer Exception on card type. In the Cards file, this card has the wrong type. " + ex);
            return R.mipmap.ic_launcher;
        }
    }

    public String getTypeName() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSpecial() {
        return special;
    }

    public int getKredits() {
        //Kredits images array. The Card class returns an image correspondent to the kredits value.
        HashMap <Integer, Integer> kreditMap = new HashMap<>();
        kreditMap.put(0, R.drawable.k0); kreditMap.put(1, R.drawable.k1);
        kreditMap.put(2, R.drawable.k2); kreditMap.put(3, R.drawable.k3);
        kreditMap.put(4, R.drawable.k4); kreditMap.put(5, R.drawable.k5);
        kreditMap.put(6, R.drawable.k6); kreditMap.put(7, R.drawable.k7);
        kreditMap.put(8, R.drawable.k8); kreditMap.put(9, R.drawable.k9);
        kreditMap.put(10, R.drawable.k10); kreditMap.put(12, R.drawable.k12);
        kreditMap.put(15, R.drawable.k15);
        try {
            //noinspection ConstantConditions
            return kreditMap.get(kredits);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "Card.java: getKredits: " + ex);
            return R.mipmap.ic_launcher;
        }
    }

    public int getKreditsNumber() {
        return kredits;
    }

    public int getOpCost() {
        return opCost;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getImage() {
        Map<String, Integer> flavorMap;
        flavorMap = new HashMap<>();
        flavorMap.put("AA BARRAGE", R.drawable.aa_barrage);
        flavorMap.put("ACTIVE SONAR", R.drawable.active_sonar);
        flavorMap.put("ADMIRAL HIPPER", R.drawable.admiral_hipper);
        flavorMap.put("ADVANCED REICH R&D", R.drawable.advanced_reich_rd);
        flavorMap.put("ADVANCED ROYAL RESEARCH", R.drawable.advanced_royal_research);
        flavorMap.put("ADVANCED US RESEARCH", R.drawable.advanced_us_research);
        flavorMap.put("AERIAL RECONNAISSANCE", R.drawable.aerial_reconnaissance);
        flavorMap.put("AIRDROP", R.drawable.airdrop);
        flavorMap.put("AIRSTRIKE", R.drawable.airstrike);
        flavorMap.put("AIR BLITZ", R.drawable.air_blitz);
        flavorMap.put("AIR DEFENSE", R.drawable.air_defense);
        flavorMap.put("AIR POWER", R.drawable.air_power);
        flavorMap.put("AIR SUPERIORITY", R.drawable.air_superiority);
        flavorMap.put("ALLIED RESEARCH EFFORT", R.drawable.allied_research_effort);
        flavorMap.put("AMPHIBIOUS ASSAULT", R.drawable.amphibious_assault);
        flavorMap.put("ANCIENT EMPIRE", R.drawable.ancient_empire);
        flavorMap.put("ANNIHILATION", R.drawable.annihilation);
        flavorMap.put("ARCTIC CONVOY", R.drawable.arctic_convoy);
        flavorMap.put("ARMING THE RESISTANCE", R.drawable.arming_the_resistance);
        flavorMap.put("FRONTAL ASSAULT", R.drawable.frontal_assault);
        flavorMap.put("ATLANTIC CONVOY", R.drawable.atlantic_convoy);
        flavorMap.put("AT ALL COST", R.drawable.at_all_cost);
        flavorMap.put("AWOKEN GIANT", R.drawable.awoken_giant);
        flavorMap.put("A FEW GOOD MEN", R.drawable.a_few_good_men);
        flavorMap.put("BARRAGE", R.drawable.barrage);
        flavorMap.put("BISMARCK", R.drawable.bismarck);
        flavorMap.put("BLACKOUT", R.drawable.blackout);
        flavorMap.put("BLADE OF THE SAMURAI", R.drawable.blade_of_the_samurai);
        flavorMap.put("BLETCHLEY PARK", R.drawable.bletchley_park);
        flavorMap.put("BLITZKRIEG", R.drawable.blitzkrieg);
        flavorMap.put("BLOCKADE", R.drawable.blockade);
        flavorMap.put("BLOODY SICKLE", R.drawable.bloody_sickle);
        flavorMap.put("BLOOD RED SKY", R.drawable.blood_red_sky);
        flavorMap.put("BOLSTER THE RANKS", R.drawable.bolster_the_ranks);
        flavorMap.put("BOMBING RAID", R.drawable.bombing_raid);
        flavorMap.put("BREAKTHROUGH", R.drawable.breakthrough);
        flavorMap.put("BURNING SKY", R.drawable.burning_sky);
        flavorMap.put("BURNING SUN", R.drawable.burning_sun);
        flavorMap.put("BURST OF FIRE", R.drawable.burst_of_fire);
        flavorMap.put("BY THE SWORD", R.drawable.by_the_sword);
        flavorMap.put("CADET NURSE CORPS", R.drawable.cadet_nurse_corps);
        flavorMap.put("CALL TO THE COLONIES", R.drawable.call_to_the_colonies);
        flavorMap.put("CAMPAIGN TRAIL", R.drawable.campaign_trail);
        flavorMap.put("CARELESS TALK", R.drawable.careless_talk);
        flavorMap.put("CARPET BOMBING", R.drawable.carpet_bombing);
        flavorMap.put("CARRIER COVER", R.drawable.carrier_cover);
        flavorMap.put("CLOSE AIR SUPPORT", R.drawable.close_air_support);
        flavorMap.put("CLOSE COMBAT", R.drawable.close_combat);
        flavorMap.put("CODE OF BUSHIDO", R.drawable.code_of_bushido);
        flavorMap.put("COLONIAL DREAMS", R.drawable.colonial_dreams);
        flavorMap.put("COMBINED ARMS", R.drawable.combined_arms);
        flavorMap.put("JUGGERNAUT", R.drawable.juggernaut);
        flavorMap.put("CONFUSION", R.drawable.confusion);
        flavorMap.put("CONSCRIPTS", R.drawable.conscripts);
        flavorMap.put("CONVOY HX 175", R.drawable.convoy_hx_175);
        flavorMap.put("COSTLY VICTORY", R.drawable.costly_victory);
        flavorMap.put("COUNTER OFFENSIVE", R.drawable.counter_offensive);
        flavorMap.put("COUNTER STRIKE", R.drawable.counter_strike);
        flavorMap.put("CRITICAL DAMAGE", R.drawable.critical_damage);
        flavorMap.put("CRITICAL HIT", R.drawable.critical_hit);
        flavorMap.put("CUP OF TEA", R.drawable.cup_of_tea);
        flavorMap.put("DAYLIGHT BOMBING", R.drawable.daylight_bombing);
        flavorMap.put("DEADLY DUTY", R.drawable.deadly_duty);
        flavorMap.put("DEATH FROM ABOVE", R.drawable.death_from_above);
        flavorMap.put("DEFEND IN DEPTH", R.drawable.defend_in_depth);
        flavorMap.put("DELAYING TACTICS", R.drawable.delaying_tactics);
        flavorMap.put("DEPTH CHARGES", R.drawable.depth_charges);
        flavorMap.put("DESERT PUSH", R.drawable.desert_push);
        flavorMap.put("THE DESERT RATS", R.drawable.the_desert_rats);
        flavorMap.put("DESPERATE MEASURES", R.drawable.desperate_measures);
        flavorMap.put("DIRECT HIT", R.drawable.direct_hit);
        flavorMap.put("DIVE BOMBING", R.drawable.dive_bombing);
        flavorMap.put("DIVINE WIND", R.drawable.divine_wind);
        flavorMap.put("DOWDING SYSTEM", R.drawable.dowding_system);
        flavorMap.put("EAGLE CLAWS", R.drawable.eagle_claws);
        flavorMap.put("EASTERN FRONT BOUND", R.drawable.eastern_front_bound);
        flavorMap.put("ELUSIVE FORCE", R.drawable.elusive_force);
        flavorMap.put("EMBARGO", R.drawable.embargo);
        flavorMap.put("EMPIRE OF THE SUN", R.drawable.empire_of_the_sun);
        flavorMap.put("ENCIRCLEMENT", R.drawable.encirclement);
        flavorMap.put("ENEMY SPOTTED", R.drawable.enemy_spotted);
        flavorMap.put("ENIGMA", R.drawable.enigma);
        flavorMap.put("ENTRAPMENT", R.drawable.entrapment);
        flavorMap.put("EVASIVE ACTION", R.drawable.evasive_action);
        flavorMap.put("EXPANDED REICH R&D", R.drawable.expanded_reich_rd);
        flavorMap.put("EXPANDED ROYAL RESEARCH", R.drawable.expanded_royal_research);
        flavorMap.put("EXPANDED US RESEARCH", R.drawable.expanded_us_research);
        flavorMap.put("EXPERIMENTAL FLIGHT", R.drawable.experimental_flight);
        flavorMap.put("EXPLOIT THE GAP", R.drawable.exploit_the_gap);
        flavorMap.put("FAST HEINZ", R.drawable.fast_heinz);
        flavorMap.put("FEIGNED RETREAT", R.drawable.feigned_retreat);
        flavorMap.put("FINAL PUSH", R.drawable.final_push);
        flavorMap.put("FINEST HOUR", R.drawable.finest_hour);
        flavorMap.put("FOG OF WAR", R.drawable.fog_of_war);
        flavorMap.put("FORTIFICATION", R.drawable.fortification);
        flavorMap.put("FORTIFIED POSITION", R.drawable.fortified_position);
        flavorMap.put("FOR FREEDOM", R.drawable.for_freedom);
        flavorMap.put("FOR GLORY", R.drawable.for_glory);
        flavorMap.put("PRECISION BOMBING", R.drawable.precision_bombing);
        flavorMap.put("FOR PROSPERITY", R.drawable.for_prosperity);
        flavorMap.put("FOR THE EMPEROR", R.drawable.for_the_emperor);
        flavorMap.put("FOR THE KING", R.drawable.for_the_king);
        flavorMap.put("WESTERN ALLIES", R.drawable.western_allies);
        flavorMap.put("FROM THE DEEP", R.drawable.from_the_deep);
        flavorMap.put("FROM THE PEOPLE", R.drawable.from_the_people);
        flavorMap.put("GREATER PURPOSE", R.drawable.greater_purpose);
        flavorMap.put("GROUNDED", R.drawable.grounded);
        flavorMap.put("GUNBOAT HIT", R.drawable.gunboat_hit);
        flavorMap.put("GUNBOAT RAID", R.drawable.gunboat_raid);
        flavorMap.put("GUNBOAT RUN", R.drawable.gunboat_run);
        flavorMap.put("GUNSHIP MISSION", R.drawable.gunship_mission);
        flavorMap.put("HEROES OF THE SOVIET UNION", R.drawable.heroes_of_the_soviet_union);
        flavorMap.put("HIGH ALTITUDE BOMBING", R.drawable.high_altitude_bombing);
        flavorMap.put("HIS MAJESTY'S CHOSEN", R.drawable.his_majestys_chosen);
        flavorMap.put("HMS ILLUSTRIOUS", R.drawable.hms_illustrious);
        flavorMap.put("HOME DEFENSE", R.drawable.home_defense);
        flavorMap.put("HONOR", R.drawable.honor);
        flavorMap.put("HONORABLE DEATH", R.drawable.honorable_death);
        flavorMap.put("IMPERIAL DECREE", R.drawable.imperial_decree);
        flavorMap.put("IMPERIAL ORDER", R.drawable.imperial_order);
        flavorMap.put("IMPERIAL STRENGTH", R.drawable.imperial_strength);
        flavorMap.put("INDUSTRIAL MIGHT", R.drawable.industrial_might);
        flavorMap.put("INTERCEPTION", R.drawable.interception);
        flavorMap.put("IN THE NAVY", R.drawable.in_the_navy);
        flavorMap.put("IRON FROM THE NORTH", R.drawable.iron_from_the_north);
        flavorMap.put("ISOLATION", R.drawable.isolation);
        flavorMap.put("HUMAN TORPEDO", R.drawable.human_torpedo);
        flavorMap.put("JOINT OPERATION", R.drawable.joint_operation);
        flavorMap.put("KAMIKAZE", R.drawable.kamikaze);
        flavorMap.put("KRIEGSMARINE", R.drawable.kriegsmarine);
        flavorMap.put("LAND OF THE FREE", R.drawable.land_of_the_free);
        flavorMap.put("LAST RITES", R.drawable.last_rites);
        flavorMap.put("LEND LEASE", R.drawable.lend_lease);
        flavorMap.put("LESSER OF TWO EVILS", R.drawable.lesser_of_two_evils);
        flavorMap.put("LIBERATION", R.drawable.liberation);
        flavorMap.put("LIGHTNING CONQUEST", R.drawable.lightning_conquest);
        flavorMap.put("LIGHTNING STRIKE", R.drawable.lightning_strike);
        flavorMap.put("LION FOR A DAY", R.drawable.lion_for_a_day);
        flavorMap.put("LONG RANGE DESERT GROUP", R.drawable.long_range_desert_group);
        flavorMap.put("LURKING DANGER", R.drawable.lurking_danger);
        flavorMap.put("MAGIC", R.drawable.magic);
        flavorMap.put("MANHATTAN PROJECT", R.drawable.manhattan_project);
        flavorMap.put("MARE NOSTRUM", R.drawable.mare_nostrum);
        flavorMap.put("MASS ATTACK", R.drawable.mass_attack);
        flavorMap.put("MEDITERRANEAN RAID", R.drawable.mediterranean_raid);
        flavorMap.put("MEN OF STEEL", R.drawable.men_of_steel);
        flavorMap.put("THE MERCHANT NAVY", R.drawable.the_merchant_navy);
        flavorMap.put("MINORITY RECRUITS", R.drawable.minority_recruits);
        flavorMap.put("MISSING", R.drawable.missing);
        flavorMap.put("MI5", R.drawable.mi5);
        flavorMap.put("STAND FAST", R.drawable.stand_fast);
        flavorMap.put("MOBILE DEFENSE", R.drawable.mobile_defense);
        flavorMap.put("MOBILIZATION", R.drawable.mobilization);
        flavorMap.put("MONTY", R.drawable.monty);
        flavorMap.put("MOTHER RUSSIA", R.drawable.mother_russia);
        flavorMap.put("MUD", R.drawable.mud);
        flavorMap.put("NATIONAL FIRE SERVICE", R.drawable.national_fire_service);
        flavorMap.put("NAVAL BOMBARDMENT", R.drawable.naval_bombardment);
        flavorMap.put("NAVAL OPERATION", R.drawable.naval_operation);
        flavorMap.put("NAVAL POWER", R.drawable.naval_power);
        flavorMap.put("NAVAL SUPPLY RUN", R.drawable.naval_supply_run);
        flavorMap.put("NAVAL SUPPORT", R.drawable.naval_support);
        flavorMap.put("NAVAL TASK FORCE", R.drawable.naval_task_force);
        flavorMap.put("NIGHT RAID", R.drawable.night_raid);
        flavorMap.put("NIGHT WITCHES", R.drawable.night_witches);
        flavorMap.put("NO RETREAT", R.drawable.no_retreat);
        flavorMap.put("NO SURRENDER", R.drawable.no_surrender);
        flavorMap.put("ORDER 227", R.drawable.order_227);
        flavorMap.put("OUTFLANK", R.drawable.outflank);
        flavorMap.put("OUTMANEUVER", R.drawable.outmaneuver);
        flavorMap.put("OVERCAST", R.drawable.overcast);
        flavorMap.put("OVERWHELMING FORCE", R.drawable.overwhelming_force);
        flavorMap.put("PACT OF STEEL", R.drawable.pact_of_steel);
        flavorMap.put("PARADE", R.drawable.parade);
        flavorMap.put("PATRIOTIC FIRESTORM", R.drawable.patriotic_firestorm);
        flavorMap.put("GREAT PATRIOTIC WAR", R.drawable.great_patriotic_war);
        flavorMap.put("PATRIOTIC ZEAL", R.drawable.patriotic_zeal);
        flavorMap.put("PATROL", R.drawable.patrol);
        flavorMap.put("PATTON", R.drawable.patton);
        flavorMap.put("PENICILLIN", R.drawable.penicillin);
        flavorMap.put("PHONEY WAR", R.drawable.phoney_war);
        flavorMap.put("PRESSURIZED CABIN", R.drawable.pressurized_cabin);
        flavorMap.put("PURSUIT", R.drawable.pursuit);
        flavorMap.put("RADAR", R.drawable.radar);
        flavorMap.put("RADAR ALERT", R.drawable.radar_alert);
        flavorMap.put("RAID", R.drawable.raid);
        flavorMap.put("RALLY", R.drawable.rally);
        flavorMap.put("RAPID ENGAGEMENT", R.drawable.rapid_engagement);
        flavorMap.put("RECKLESS ASSAULT", R.drawable.reckless_assault);
        flavorMap.put("LONG RANGE RECON", R.drawable.long_range_recon);
        flavorMap.put("RED BANNER", R.drawable.red_banner);
        flavorMap.put("RED OCTOBER", R.drawable.red_october);
        flavorMap.put("REICHSBANK", R.drawable.reichsbank);
        flavorMap.put("AFRIKA KORPS", R.drawable.afrika_korps);
        flavorMap.put("REICH RESEARCH", R.drawable.reich_research);
        flavorMap.put("REINFORCEMENTS", R.drawable.reinforcements);
        flavorMap.put("REORGANIZE", R.drawable.reorganize);
        flavorMap.put("RESCUE MISSION", R.drawable.rescue_mission);
        flavorMap.put("RESERVES", R.drawable.reserves);
        flavorMap.put("RESISTANCE", R.drawable.resistance);
        flavorMap.put("RESOLUTE DEFENSE", R.drawable.resolute_defense);
        flavorMap.put("RETALIATION", R.drawable.retaliation);
        flavorMap.put("RISING SUN", R.drawable.rising_sun);
        flavorMap.put("ROOT OUT THE ENEMY", R.drawable.root_out_the_enemy);
        flavorMap.put("ROYAL RESEARCH", R.drawable.royal_research);
        flavorMap.put("RULE BRITANNIA", R.drawable.rule_britannia);
        flavorMap.put("RUSH PRODUCTION", R.drawable.rush_production);
        flavorMap.put("SABOTAGE", R.drawable.sabotage);
        flavorMap.put("SCORCHED EARTH", R.drawable.scorched_earth);
        flavorMap.put("SCOUTING PARTY", R.drawable.scouting_party);
        flavorMap.put("SEABORNE INVASION", R.drawable.seaborne_invasion);
        flavorMap.put("NIGHT HUNTERS", R.drawable.night_hunters);
        flavorMap.put("SEA PATROL", R.drawable.sea_patrol);
        flavorMap.put("SECOND CHANCE", R.drawable.second_chance);
        flavorMap.put("SECRET OPERATIVES", R.drawable.secret_operatives);
        flavorMap.put("TAKE INITIATIVE", R.drawable.take_initiative);
        flavorMap.put("SHELLING", R.drawable.shelling);
        flavorMap.put("SHORE BOMBARDMENT", R.drawable.shore_bombardment);
        flavorMap.put("SIBERIAN TRANSFER", R.drawable.siberian_transfer);
        flavorMap.put("SKY BARONS", R.drawable.sky_barons);
        flavorMap.put("SNEAK ATTACK", R.drawable.sneak_attack);
        flavorMap.put("SNEAK MANEUVER", R.drawable.sneak_maneuver);
        flavorMap.put("SNIPED", R.drawable.sniped);
        flavorMap.put("SORTIE", R.drawable.sortie);
        flavorMap.put("SOUL OF OLD JAPAN", R.drawable.soul_of_old_japan);
        flavorMap.put("SPECIAL ASSIGNMENT", R.drawable.special_assignment);
        flavorMap.put("SPECIAL DELIVERY", R.drawable.special_delivery);
        flavorMap.put("SPIRIT OF ROME", R.drawable.spirit_of_rome);
        flavorMap.put("SPOILS OF WAR", R.drawable.spoils_of_war);
        flavorMap.put("SPY RING", R.drawable.spy_ring);
        flavorMap.put("STRAFING", R.drawable.strafing);
        flavorMap.put("STRANGLEHOLD", R.drawable.stranglehold);
        flavorMap.put("STRATEGIC BOMBING", R.drawable.strategic_bombing);
        flavorMap.put("STRATEGIC PLANNING", R.drawable.strategic_planning);
        flavorMap.put("SUDDEN STRIKE", R.drawable.sudden_strike);
        flavorMap.put("SUPPLY CHAIN", R.drawable.supply_chain);
        flavorMap.put("SUPPLY DROP", R.drawable.supply_drop);
        flavorMap.put("SUPPLY FRONT", R.drawable.supply_front);
        flavorMap.put("SUPPLY SHIPMENT", R.drawable.supply_shipment);
        flavorMap.put("SUPPRESSION", R.drawable.suppression);
        flavorMap.put("SURPRISE ATTACK", R.drawable.surprise_attack);
        flavorMap.put("SYNTHETIC OIL", R.drawable.synthetic_oil);
        flavorMap.put("SYNTHETIC RUBBER", R.drawable.synthetic_rubber);
        flavorMap.put("TACTICAL STRIKE", R.drawable.tactical_strike);
        flavorMap.put("TACTICAL WITHDRAWAL", R.drawable.tactical_withdrawal);
        flavorMap.put("TAMANSKAYA!", R.drawable.tamanskaya);
        flavorMap.put("TARGET ACQUIRED", R.drawable.target_acquired);
        flavorMap.put("VIVE LA RESISTANCE!", R.drawable.vive_la_resistance);
        flavorMap.put("THE ALLIANCE", R.drawable.the_alliance);
        flavorMap.put("THE COMMONWEALTH", R.drawable.the_commonwealth);
        flavorMap.put("THE EMPIRE STRIKES", R.drawable.the_empire_strikes);
        flavorMap.put("THE HAMMER", R.drawable.the_hammer);
        flavorMap.put("THE WAR MACHINE", R.drawable.the_war_machine);
        flavorMap.put("TORA! TORA! TORA!", R.drawable.tora_tora_tora);
        flavorMap.put("TORPEDO ATTACK", R.drawable.torpedo_attack);
        flavorMap.put("TO THE LAST MAN", R.drawable.to_the_last_man);
        flavorMap.put("TRACTOR FACTORIES", R.drawable.tractor_factories);
        flavorMap.put("TYPE XXI U-BOAT", R.drawable.type_xxi_uboat);
        flavorMap.put("ULTRA", R.drawable.ultra);
        flavorMap.put("UNCLE SAM", R.drawable.uncle_sam);
        flavorMap.put("UNEXPECTED RESISTANCE", R.drawable.unexpected_resistance);
        flavorMap.put("UNITY IS STRENGTH", R.drawable.unity_is_strength);
        flavorMap.put("URA!", R.drawable.ura);
        flavorMap.put("URAL FACTORIES", R.drawable.ural_factories);
        flavorMap.put("URANPROJEKT", R.drawable.uranprojekt);
        flavorMap.put("USACE", R.drawable.usace);
        flavorMap.put("US MILITARY RESEARCH", R.drawable.us_military_research);
        flavorMap.put("U-16", R.drawable.u16);
        flavorMap.put("U-375", R.drawable.u375);
        flavorMap.put("U-48", R.drawable.u48);
        flavorMap.put("V-1 FLYING BOMB", R.drawable.v1_flying_bomb);
        flavorMap.put("VETERAN PILOTS", R.drawable.veteran_pilots);
        flavorMap.put("WAR BONDS", R.drawable.war_bonds);
        flavorMap.put("WAR PRODUCTION", R.drawable.war_production);
        flavorMap.put("WE CAN DO IT!", R.drawable.we_can_do_it);
        flavorMap.put("WINTER OFFENSIVE", R.drawable.winter_offensive);
        flavorMap.put("WINTER WARFARE", R.drawable.winter_warfare);
        flavorMap.put("WOLFPACK", R.drawable.wolfpack);
        flavorMap.put("WORKERS UNITE!", R.drawable.workers_unite);
        flavorMap.put("ADMIRAL YAMAMOTO", R.drawable.admiral_yamamoto);
        flavorMap.put("YAMATO", R.drawable.yamato);
        flavorMap.put("ZHUKOV", R.drawable.zhukov);
        flavorMap.put("DANZIG", R.drawable.danzig);
        flavorMap.put("TRUK", R.drawable.truk);
        flavorMap.put("CHERBOURG", R.drawable.cherbourg);
        flavorMap.put("EL ALAMEIN", R.drawable.el_alamein);
        flavorMap.put("GUADALCANAL", R.drawable.guadalcanal);
        flavorMap.put("ALEXANDRIA", R.drawable.alexandria);
        flavorMap.put("MANILA", R.drawable.manila);
        flavorMap.put("MOSCOW", R.drawable.moscow);
        flavorMap.put("STALINGRAD", R.drawable.stalingrad);
        flavorMap.put("TUNIS CITY", R.drawable.tunis_city);
        flavorMap.put("1005th RIFLES", R.drawable.m1005th_rifles);
        flavorMap.put("101st AIRBORNE", R.drawable.m101st_airborne);
        flavorMap.put("106th QUARTERMASTERS", R.drawable.m106th_quartermasters);
        flavorMap.put("109th COMBAT ENGINEERS", R.drawable.m109th_combat_engineers);
        flavorMap.put("10th ENGINEERS BATTALION", R.drawable.m10th_engineers_battalion);
        flavorMap.put("10th GUARDS REGIMENT", R.drawable.m10th_guards_regiment);
        flavorMap.put("10.5 cm leFH", R.drawable.m10_5_cm_lefh);
        flavorMap.put("111th INDIAN BRIGADE", R.drawable.m111th_indian_brigade);
        flavorMap.put("71st ZHYTOMYR", R.drawable.m71st_zhytomyr);
        flavorMap.put("11th INFANTRY REGIMENT", R.drawable.m11th_infantry_regiment);
        flavorMap.put("329th ENGINEER BATTALION", R.drawable.m329th_engineer_battalion);
        flavorMap.put("120mm M1 GUN", R.drawable.m120mm_m1_gun);
        flavorMap.put("1271st RIFLES", R.drawable.m1271st_rifles);
        flavorMap.put("128th RIFLES", R.drawable.m128th_rifles);
        flavorMap.put("12th INFANTRY REGIMENT", R.drawable.m12th_infantry_regiment);
        flavorMap.put("13th RIFLE REGIMENT", R.drawable.m13th_rifle_regiment);
        flavorMap.put("141. GEBIRGSJÄGER", R.drawable.m141_gebirgsjger);
        flavorMap.put("144th INFANTRY REGIMENT", R.drawable.m144th_infantry_regiment);
        flavorMap.put("155 C MODÈLE 1917", R.drawable.m155_c_modle_1917);
        flavorMap.put("158. NACHSCHUB", R.drawable.m158_nachschub);
        flavorMap.put("15th CAVALRY REGIMENT", R.drawable.m15th_cavalry_regiment);
        flavorMap.put("15 cm AUTOKANONE", R.drawable.m15_cm_autokanone);
        flavorMap.put("164th INFANTRY REGIMENT", R.drawable.m164th_infantry_regiment);
        flavorMap.put("16th RIFLES", R.drawable.m16th_rifles);
        flavorMap.put("17th INFANTRY REGIMENT", R.drawable.m17th_infantry_regiment);
        flavorMap.put("17th RIFLE REGIMENT", R.drawable.m17th_rifle_regiment);
        flavorMap.put("17 POUNDER", R.drawable.m17_pounder);
        flavorMap.put("186th FOLGORE", R.drawable.m186th_folgore);
        flavorMap.put("155mm 1918 MODEL", R.drawable.m155mm_1918_model);
        flavorMap.put("1er RÉGIMENT ÉTRANGER", R.drawable.m1er_rgiment_tranger);
        flavorMap.put("1st AIRBORNE", R.drawable.m1st_airborne);
        flavorMap.put("1st AIRLANDING BRIGADE", R.drawable.m1st_airlanding_brigade);
        flavorMap.put("1. INFANTRY REGIMENT", R.drawable.m1_infantry_regiment);
        flavorMap.put("1st MARINES", R.drawable.m1st_marines);
        flavorMap.put("1st SIGNAL REGIMENT", R.drawable.m1st_signal_regiment);
        flavorMap.put("1st TAIPEI REGIMENT", R.drawable.m1st_taipei_regiment);
        flavorMap.put("1st YOKOSUKA", R.drawable.m1st_yokosuka);
        flavorMap.put("222nd GUARD RIFLES", R.drawable.m222nd_guard_rifles);
        flavorMap.put("22nd GUARDS BRIGADE", R.drawable.m22nd_guards_brigade);
        flavorMap.put("22. INFANTRY REGIMENT", R.drawable.m22_infantry_regiment);
        flavorMap.put("25th INFANTRY REGIMENT", R.drawable.m25th_infantry_regiment);
        flavorMap.put("25. PANZERGRENADIER", R.drawable.m25_panzergrenadier);
        flavorMap.put("25 POUNDER", R.drawable.m25_pounder);
        flavorMap.put("28cm COASTAL HOWITZER", R.drawable.m28cm_coastal_howitzer);
        flavorMap.put("2e BRIGADE", R.drawable.m2e_brigade);
        flavorMap.put("2nd ALPINI REGIMENT", R.drawable.m2nd_alpini_regiment);
        flavorMap.put("2nd RIFLES", R.drawable.m2nd_rifles);
        flavorMap.put("2nd PARACHUTE", R.drawable.m2nd_parachute);
        flavorMap.put("2nd RAIDING BRIGADE", R.drawable.m2nd_raiding_brigade);
        flavorMap.put("2/5 MARINES", R.drawable.m25_marines);
        flavorMap.put("2 POUNDER", R.drawable.m2_pounder);
        flavorMap.put("30th REGIMENT", R.drawable.m30th_regiment);
        flavorMap.put("321st RIFLE REGIMENT", R.drawable.m321st_rifle_regiment);
        flavorMap.put("32nd INFANTRY REGIMENT", R.drawable.m32nd_infantry_regiment);
        flavorMap.put("332nd ENGINEER REGIMENT", R.drawable.m332nd_engineer_regiment);
        flavorMap.put("33rd LIVORNO REGIMENT", R.drawable.m33rd_livorno_regiment);
        flavorMap.put("33rd RECON", R.drawable.m33rd_recon);
        flavorMap.put("33. PANZERGRENADIER", R.drawable.m33_panzergrenadier);
        flavorMap.put("34th GUARDS", R.drawable.m34th_guards);
        flavorMap.put("34th INFANTRY REGIMENT", R.drawable.m34th_infantry_regiment);
        flavorMap.put("35th MOUNTAIN RIFLES", R.drawable.m35th_mountain_rifles);
        flavorMap.put("35th RIFLE REGIMENT", R.drawable.m35th_rifle_regiment);
        flavorMap.put("361. AFRIKA REGIMENT", R.drawable.m361_afrika_regiment);
        flavorMap.put("39th BOLOGNA REGIMENT", R.drawable.m39th_bologna_regiment);
        flavorMap.put("3rd ALPINI REGIMENT", R.drawable.m3rd_alpini_regiment);
        flavorMap.put("3rd GUARDS REGIMENT", R.drawable.m3rd_guards_regiment);
        flavorMap.put("3.7” ANTI-AIR GUN", R.drawable.m3_7_antiair_gun);
        flavorMap.put("3. FALLSCHIRMJÄGER", R.drawable.m3_fallschirmjger);
        flavorMap.put("41st BICYCLE REGIMENT", R.drawable.m41st_bicycle_regiment);
        flavorMap.put("42nd RIFLES", R.drawable.m42nd_rifles);
        flavorMap.put("456th RIFLE REGIMENT", R.drawable.m456th_rifle_regiment);
        flavorMap.put("45 mm ANTI-TANK GUN", R.drawable.m45_mm_antitank_gun);
        flavorMap.put("463rd BATTALION", R.drawable.m463rd_battalion);
        flavorMap.put("47th INFANTRY REGIMENT", R.drawable.m47th_infantry_regiment);
        flavorMap.put("48e RÉGIMENT D'INFANTERIE", R.drawable.m48e_rgiment_dinfanterie);
        flavorMap.put("4th MARINES", R.drawable.m4th_marines);
        flavorMap.put("BL 4.5\" MEDIUM GUN", R.drawable.bl_4_5_medium_gun);
        flavorMap.put("4. PIONEER BATTALION", R.drawable.m4_pioneer_battalion);
        flavorMap.put("503rd MOTORS", R.drawable.m503rd_motors);
        flavorMap.put("506th AIRBORNE", R.drawable.m506th_airborne);
        flavorMap.put("56. JÄGER REGIMENT", R.drawable.m56_jger_regiment);
        flavorMap.put("554th RIFLE REGIMENT", R.drawable.m554th_rifle_regiment);
        flavorMap.put("ZiS-2", R.drawable.zis2);
        flavorMap.put("5th BRIGADE", R.drawable.m5th_brigade);
        flavorMap.put("5. PANZERGRENADIER", R.drawable.m5_panzergrenadier);
        flavorMap.put("60th INFANTRY REGIMENT", R.drawable.m60th_infantry_regiment);
        flavorMap.put("61st INFANTRY REGIMENT", R.drawable.m61st_infantry_regiment);
        flavorMap.put("6th AIRBORNE", R.drawable.m6th_airborne);
        flavorMap.put("6th ALPINI REGIMENT", R.drawable.m6th_alpini_regiment);
        flavorMap.put("6th INFANTRY REGIMENT", R.drawable.m6th_infantry_regiment);
        flavorMap.put("15th MOTOR RIFLES", R.drawable.m15th_motor_rifles);
        flavorMap.put("6th NAVAL BRIGADE", R.drawable.m6th_naval_brigade);
        flavorMap.put("6 POUNDER", R.drawable.m6_pounder);
        flavorMap.put("73e RÉGIMENT D'INFANTERIE", R.drawable.m73e_rgiment_dinfanterie);
        flavorMap.put("756th REGIMENT", R.drawable.m756th_regiment);
        flavorMap.put("75mm FIELD ARTILLERY", R.drawable.m75mm_field_artillery);
        flavorMap.put("75mm MOUNTAIN GUN", R.drawable.m75mm_mountain_gun);
        flavorMap.put("75th RANGERS", R.drawable.m75th_rangers);
        flavorMap.put("75mm PACK HOWITZER", R.drawable.m75mm_pack_howitzer);
        flavorMap.put("76 mm HOWITZER 1939", R.drawable.m76_mm_howitzer_1939);
        flavorMap.put("7th REGIMENT", R.drawable.m7th_regiment);
        flavorMap.put("81. INFANTRY REGIMENT", R.drawable.m81_infantry_regiment);
        flavorMap.put("83rd NAVAL BRIGADE", R.drawable.m83rd_naval_brigade);
        flavorMap.put("845th RIFLES", R.drawable.m845th_rifles);
        flavorMap.put("84th INFANTRY REGIMENT", R.drawable.m84th_infantry_regiment);
        flavorMap.put("85mm D-44 FIELD GUN", R.drawable.m85mm_d44_field_gun);
        flavorMap.put("85 PIONEER COMPANY", R.drawable.m85_pioneer_company);
        flavorMap.put("87. GRENADIER", R.drawable.m87_grenadier);
        flavorMap.put("88 mm FLAK", R.drawable.m88_mm_flak);
        flavorMap.put("89th INFANTRY REGIMENT", R.drawable.m89th_infantry_regiment);
        flavorMap.put("8th CAVALRY REGIMENT", R.drawable.m8th_cavalry_regiment);
        flavorMap.put("1st RIFLES", R.drawable.m1st_rifles);
        flavorMap.put("95th RIFLE REGIMENT", R.drawable.m95th_rifle_regiment);
        flavorMap.put("99th INFANTRY BATTALION", R.drawable.m99th_infantry_battalion);
        flavorMap.put("A-20 HAVOC", R.drawable.a20_havoc);
        flavorMap.put("A-20B", R.drawable.a20b);
        flavorMap.put("A-26 INVADER", R.drawable.a26_invader);
        flavorMap.put("A5M4 CLAUDE", R.drawable.a5m4_claude);
        flavorMap.put("A6M3 ZEKE", R.drawable.a6m3_zeke);
        flavorMap.put("GREIF", R.drawable.greif);
        flavorMap.put("AICHI D3A-2", R.drawable.aichi_d3a2);
        flavorMap.put("AKITA REGIMENT", R.drawable.akita_regiment);
        flavorMap.put("ALBACORE Mk I", R.drawable.albacore_mk_i);
        flavorMap.put("ARADO AR 196", R.drawable.arado_ar_196);
        flavorMap.put("ARGYLLSHIRE HIGHLANDERS", R.drawable.argyllshire_highlanders);
        flavorMap.put("BP-43 ARMORED TRAIN", R.drawable.bp43_armored_train);
        flavorMap.put("AVRO ANSON", R.drawable.avro_anson);
        flavorMap.put("A-24 BANSHEE", R.drawable.a24_banshee);
        flavorMap.put("A-36 APACHE", R.drawable.a36_apache);
        flavorMap.put("B-4 203mm HOWITZER", R.drawable.b4_203mm_howitzer);
        flavorMap.put("B6N TENZAN", R.drawable.b6n_tenzan);
        flavorMap.put("BALUCH REGIMENT", R.drawable.baluch_regiment);
        flavorMap.put("BLACK WATCH", R.drawable.black_watch);
        flavorMap.put("BLENHEIM Mk IV", R.drawable.blenheim_mk_iv);
        flavorMap.put("BLOCH MB.152", R.drawable.bloch_mb_152);
        flavorMap.put("BLOHM & VOSS BV 138", R.drawable.blohm_voss_bv_138);
        flavorMap.put("M4 FIREFLY", R.drawable.m4_firefly);
        flavorMap.put("BT-7", R.drawable.bt7);
        flavorMap.put("B-17 FLYING FORTRESS", R.drawable.b17_flying_fortress);
        flavorMap.put("B-17F", R.drawable.b17f);
        flavorMap.put("B-24 LIBERATOR", R.drawable.b24_liberator);
        flavorMap.put("B-25 H", R.drawable.b25_h);
        flavorMap.put("B-25 MITCHELL", R.drawable.b25_mitchell);
        flavorMap.put("B-26 GROUPE BRETAGNE", R.drawable.b26_groupe_bretagne);
        flavorMap.put("B-26 MARAUDER", R.drawable.b26_marauder);
        flavorMap.put("B-29 SUPERFORTRESS", R.drawable.b29_superfortress);
        flavorMap.put("C-47 SKYTRAIN", R.drawable.c47_skytrain);
        flavorMap.put("C6N SAIUN", R.drawable.c6n_saiun);
        flavorMap.put("CHAR B1 bis", R.drawable.char_b1_bis);
        flavorMap.put("CHURCHILL Mk IV", R.drawable.churchill_mk_iv);
        flavorMap.put("9.2” COASTAL GUN", R.drawable.m9_2_coastal_gun);
        flavorMap.put("COLDSTREAM GUARDS", R.drawable.coldstream_guards);
        flavorMap.put("KOMET", R.drawable.komet);
        flavorMap.put("A34 COMET", R.drawable.a34_comet);
        flavorMap.put("No. 10 COMMANDO", R.drawable.no_10_commando);
        flavorMap.put("CORSAIR F4U-1C", R.drawable.corsair_f4u1c);
        flavorMap.put("CORSAIR F4U-1D", R.drawable.corsair_f4u1d);
        flavorMap.put("4th KUBAN COSSACKS", R.drawable.m4th_kuban_cossacks);
        flavorMap.put("CROMWELL Mk IV", R.drawable.cromwell_mk_iv);
        flavorMap.put("CRUISER Mk III", R.drawable.cruiser_mk_iii);
        flavorMap.put("CRUSADER Mk II", R.drawable.crusader_mk_ii);
        flavorMap.put("D4Y SUISEI", R.drawable.d4y_suisei);
        flavorMap.put("HUMBER Mk II", R.drawable.humber_mk_ii);
        flavorMap.put("DEWOITINE D.520", R.drawable.dewoitine_d_520);
        flavorMap.put("M6A1 SEIRAN", R.drawable.m6a1_seiran);
        flavorMap.put("Ki-46 DINAH III", R.drawable.ki46_dinah_iii);
        flavorMap.put("DORNIER DO 217", R.drawable.dornier_do_217);
        flavorMap.put("EAST SURREY REGIMENT", R.drawable.east_surrey_regiment);
        flavorMap.put("F4F-3 WILDCAT", R.drawable.f4f3_wildcat);
        flavorMap.put("F4F-4 WILDCAT", R.drawable.f4f4_wildcat);
        flavorMap.put("8. FALLSCHIRMJAGER", R.drawable.m8_fallschirmjager);
        flavorMap.put("FIAT C.R.42", R.drawable.fiat_c_r_42);
        flavorMap.put("FIAT G.50", R.drawable.fiat_g_50);
        flavorMap.put("FIAT G.55", R.drawable.fiat_g_55);
        flavorMap.put("3.7cm FLAK 37", R.drawable.m3_7cm_flak_37);
        flavorMap.put("FLAMMPANZER", R.drawable.flammpanzer);
        flavorMap.put("FRENCH 75", R.drawable.french_75);
        flavorMap.put("EXPEDITIONARY CORPS", R.drawable.expeditionary_corps);
        flavorMap.put("H75-C1", R.drawable.h75c1);
        flavorMap.put("FW 190 A", R.drawable.fw_190_a);
        flavorMap.put("FW 190 JAGDBOMBER", R.drawable.fw_190_jagdbomber);
        flavorMap.put("FW 200 CONDOR", R.drawable.fw_200_condor);
        flavorMap.put("G4M1 BETTY", R.drawable.g4m1_betty);
        flavorMap.put("GLADIATOR Mk I", R.drawable.gladiator_mk_i);
        flavorMap.put("GLADIATOR ESCORT", R.drawable.gladiator_escort);
        flavorMap.put("980. VOLKSGRENADIER", R.drawable.m980_volksgrenadier);
        flavorMap.put("GRENADIER GUARDS", R.drawable.grenadier_guards);
        flavorMap.put("F6F HELLCAT", R.drawable.f6f_hellcat);
        flavorMap.put("HAMPDEN Mk I", R.drawable.hampden_mk_i);
        flavorMap.put("Ki-43 HAYABUSA", R.drawable.ki43_hayabusa);
        flavorMap.put("HEINKEL HE 219", R.drawable.heinkel_he_219);
        flavorMap.put("HENSCHEL HS 123", R.drawable.henschel_hs_123);
        flavorMap.put("HENSCHEL HS 126", R.drawable.henschel_hs_126);
        flavorMap.put("HIMEJI REGIMENT", R.drawable.himeji_regiment);
        flavorMap.put("HOTCHKISS H35", R.drawable.hotchkiss_h35);
        flavorMap.put("HUDSON Mk III", R.drawable.hudson_mk_iii);
        flavorMap.put("HUMMEL", R.drawable.hummel);
        flavorMap.put("HURRICANE Mk I", R.drawable.hurricane_mk_i);
        flavorMap.put("HURRICANE Mk IIA", R.drawable.hurricane_mk_iia);
        flavorMap.put("IIéme RMT", R.drawable.iime_rmt);
        flavorMap.put("IRISH GUARDS", R.drawable.irish_guards);
        flavorMap.put("ISU-152", R.drawable.isu152);
        flavorMap.put("IOSEF STALIN II", R.drawable.iosef_stalin_ii);
        flavorMap.put("SAVOIA CAVALLERIA", R.drawable.savoia_cavalleria);
        flavorMap.put("I-15 CHAIKA", R.drawable.i15_chaika);
        flavorMap.put("I-16 ISHAK", R.drawable.i16_ishak);
        flavorMap.put("J1N1 GEKKO", R.drawable.j1n1_gekko);
        flavorMap.put("JADE DIVISION", R.drawable.jade_division);
        flavorMap.put("JAGDPANTHER", R.drawable.jagdpanther);
        flavorMap.put("Ki-100 GOSHIKISEN", R.drawable.ki100_goshikisen);
        flavorMap.put("JUNKERS JU 188 E", R.drawable.junkers_ju_188_e);
        flavorMap.put("JUNKERS JU 88 A", R.drawable.junkers_ju_88_a);
        flavorMap.put("Ju 87 B2", R.drawable.ju_87_b2);
        flavorMap.put("JU 87 B STUKA", R.drawable.ju_87_b_stuka);
        flavorMap.put("KAGOSHIMA REGIMENT", R.drawable.kagoshima_regiment);
        flavorMap.put("KATYUSHA", R.drawable.katyusha);
        flavorMap.put("KAWANISHI H6K", R.drawable.kawanishi_h6k);
        flavorMap.put("Ki-27 NATE", R.drawable.ki27_nate);
        flavorMap.put("Ki-44 TOJO", R.drawable.ki44_tojo);
        flavorMap.put("Ki-51 SONIA", R.drawable.ki51_sonia);
        flavorMap.put("Ki-61 HIEN", R.drawable.ki61_hien);
        flavorMap.put("Ki-61-II TONY", R.drawable.ki61ii_tony);
        flavorMap.put("Ki-83", R.drawable.ki83);
        flavorMap.put("Ki-84 FRANK", R.drawable.ki84_frank);
        flavorMap.put("KUMAMOTO REGIMENT", R.drawable.kumamoto_regiment);
        flavorMap.put("KV-1 1939", R.drawable.kv1_1939);
        flavorMap.put("KV-1 1941", R.drawable.kv1_1941);
        flavorMap.put("KV-2", R.drawable.kv2);
        flavorMap.put("L6/40", R.drawable.l640);
        flavorMap.put("LANCASHIRE FUSILIERS", R.drawable.lancashire_fusiliers);
        flavorMap.put("LANCASTER Mk I", R.drawable.lancaster_mk_i);
        flavorMap.put("LANCASTER B.III", R.drawable.lancaster_b_iii);
        flavorMap.put("LA-5", R.drawable.la5);
        flavorMap.put("LA DECIMA", R.drawable.la_decima);
        flavorMap.put("37 mm ANTI-AIRCRAFT GUN", R.drawable.m37_mm_antiaircraft_gun);
        flavorMap.put("LIGHT INFANTRY", R.drawable.light_infantry);
        flavorMap.put("M10 WOLVERINE", R.drawable.m10_wolverine);
        flavorMap.put("M13/40", R.drawable.m1340);
        flavorMap.put("M16 HALF-TRACK", R.drawable.m16_halftrack);
        flavorMap.put("M18 HELLCAT", R.drawable.m18_hellcat);
        flavorMap.put("LONG TOM", R.drawable.long_tom);
        flavorMap.put("M24 CHAFFEE", R.drawable.m24_chaffee);
        flavorMap.put("M26 PERSHING", R.drawable.m26_pershing);
        flavorMap.put("M36 JACKSON", R.drawable.m36_jackson);
        flavorMap.put("M3A3 HONEY", R.drawable.m3a3_honey);
        flavorMap.put("M5 STUART", R.drawable.m5_stuart);
        flavorMap.put("M4A1", R.drawable.m4a1);
        flavorMap.put("M4 SHERMAN", R.drawable.m4_sherman);
        flavorMap.put("M4A3R3 ZIPPO", R.drawable.m4a3r3_zippo);
        flavorMap.put("M7 PRIEST", R.drawable.m7_priest);
        flavorMap.put("M8 GREYHOUND", R.drawable.m8_greyhound);
        flavorMap.put("M8 HOWITZER", R.drawable.m8_howitzer);
        flavorMap.put("MACCHI C.202", R.drawable.macchi_c_202);
        flavorMap.put("MANCHESTER IA", R.drawable.manchester_ia);
        flavorMap.put("MARDER III H", R.drawable.marder_iii_h);
        flavorMap.put("3rd MARINE REGIMENT", R.drawable.m3rd_marine_regiment);
        flavorMap.put("MATILDA Mk II", R.drawable.matilda_mk_ii);
        flavorMap.put("ME 262A SCHWALBE", R.drawable.me_262a_schwalbe);
        flavorMap.put("ME 410 HORNISSE", R.drawable.me_410_hornisse);
        flavorMap.put("ME BF 109 E", R.drawable.me_bf_109_e);
        flavorMap.put("ME BF 110", R.drawable.me_bf_110);
        flavorMap.put("MIG 3", R.drawable.mig_3);
        flavorMap.put("MIKAWA REGIMENT", R.drawable.mikawa_regiment);
        flavorMap.put("MITO REGIMENT", R.drawable.mito_regiment);
        flavorMap.put("MODEL 25", R.drawable.model_25);
        flavorMap.put("MOSQUITO FB Mk VI", R.drawable.mosquito_fb_mk_vi);
        flavorMap.put("5th INFANTRY REGIMENT", R.drawable.m5th_infantry_regiment);
        flavorMap.put("M.S.406", R.drawable.m_s_406);
        flavorMap.put("B5N KATE", R.drawable.b5n_kate);
        flavorMap.put("NEBELWERFER 42", R.drawable.nebelwerfer_42);
        flavorMap.put("No. 1 COMMANDO", R.drawable.no_1_commando);
        flavorMap.put("No. 43 COMMANDO", R.drawable.no_43_commando);
        flavorMap.put("No. 3 COMMANDO", R.drawable.no_3_commando);
        flavorMap.put("OBICE DA 75/13", R.drawable.obice_da_7513);
        flavorMap.put("OSAKA REGIMENT", R.drawable.osaka_regiment);
        flavorMap.put("P1Y GINGA", R.drawable.p1y_ginga);
        flavorMap.put("P-38 LIGHTNING", R.drawable.p38_lightning);
        flavorMap.put("RAAF LIGHTNING F-4", R.drawable.raaf_lightning_f4);
        flavorMap.put("P-39 AIRACOBRA", R.drawable.p39_airacobra);
        flavorMap.put("P-40 N-5", R.drawable.p40_n5);
        flavorMap.put("P-40 KITTYHAWK", R.drawable.p40_kittyhawk);
        flavorMap.put("P-40 WARHAWK", R.drawable.p40_warhawk);
        flavorMap.put("P-47D THUNDERBOLT", R.drawable.p47d_thunderbolt);
        flavorMap.put("P-51 MUSTANG", R.drawable.p51_mustang);
        flavorMap.put("PAK 36", R.drawable.pak_36);
        flavorMap.put("PAK 38", R.drawable.pak_38);
        flavorMap.put("PAK 40", R.drawable.pak_40);
        flavorMap.put("PANTHER G", R.drawable.panther_g);
        flavorMap.put("PANTHER A", R.drawable.panther_a);
        flavorMap.put("59. PANZERGRENADIER", R.drawable.m59_panzergrenadier);
        flavorMap.put("PANZERZUG 61 BP42", R.drawable.panzerzug_61_bp42);
        flavorMap.put("Pz. BEFEHLSWAGEN 35t", R.drawable.pz_befehlswagen_35t);
        flavorMap.put("PANZER 35(t)", R.drawable.panzer_35t);
        flavorMap.put("PANZER 38(t)", R.drawable.panzer_38t);
        flavorMap.put("PANZER III-L", R.drawable.panzer_iiil);
        flavorMap.put("PANZER III-F", R.drawable.panzer_iiif);
        flavorMap.put("PANZER III-G", R.drawable.panzer_iiig);
        flavorMap.put("PANZER III-H", R.drawable.panzer_iiih);
        flavorMap.put("PANZER III-J", R.drawable.panzer_iiij);
        flavorMap.put("PANZER III-E", R.drawable.panzer_iiie);
        flavorMap.put("PANZER II-A", R.drawable.panzer_iia);
        flavorMap.put("PANZER II-C", R.drawable.panzer_iic);
        flavorMap.put("PANZER IV F2", R.drawable.panzer_iv_f2);
        flavorMap.put("PANZER IV-G", R.drawable.panzer_ivg);
        flavorMap.put("PBY CATALINA", R.drawable.pby_catalina);
        flavorMap.put("F1M2 PETE", R.drawable.f1m2_pete);
        flavorMap.put("PETLYAKOV PE-2", R.drawable.petlyakov_pe2);
        flavorMap.put("PETLYAKOV Pe-2FT", R.drawable.petlyakov_pe2ft);
        flavorMap.put("POLIKARPOV PO-2", R.drawable.polikarpov_po2);
        flavorMap.put("POTEZ 63.11", R.drawable.potez_63_11);
        flavorMap.put("P-40B TOMAHAWK", R.drawable.p40b_tomahawk);
        flavorMap.put("P-61 BLACK WIDOW", R.drawable.p61_black_widow);
        flavorMap.put("QF 40mm Mk III", R.drawable.qf_40mm_mk_iii);
        flavorMap.put("RAF GROUND CREW", R.drawable.raf_ground_crew);
        flavorMap.put("LEOPOLD", R.drawable.leopold);
        flavorMap.put("26 ENGINEER REGIMENT", R.drawable.m26_engineer_regiment);
        flavorMap.put("ROYAL SCOTS", R.drawable.royal_scots);
        flavorMap.put("ROYAL ULSTER RIFLES", R.drawable.royal_ulster_rifles);
        flavorMap.put("ROYAL WEST KENTS", R.drawable.royal_west_kents);
        flavorMap.put("Ki-21 SALLY", R.drawable.ki21_sally);
        flavorMap.put("SBD 3 DAUNTLESS", R.drawable.sbd_3_dauntless);
        flavorMap.put("Sd KFz 222", R.drawable.sd_kfz_222);
        flavorMap.put("SEAFORTH HIGHLANDERS", R.drawable.seaforth_highlanders);
        flavorMap.put("SEMOVENTE DA 75/18", R.drawable.semovente_da_7518);
        flavorMap.put("SENDAI REGIMENT", R.drawable.sendai_regiment);
        flavorMap.put("SEXTON", R.drawable.sexton);
        flavorMap.put("SHIBATA REGIMENT", R.drawable.shibata_regiment);
        flavorMap.put("N1K-J SHIDEN", R.drawable.n1kj_shiden);
        flavorMap.put("SUNDERLAND Mk V", R.drawable.sunderland_mk_v);
        flavorMap.put("WELLINGTON", R.drawable.wellington);
        flavorMap.put("BLACKBURN SKUA Mk II", R.drawable.blackburn_skua_mk_ii);
        flavorMap.put("SAVOIA-MARCHETTI SM.79", R.drawable.savoiamarchetti_sm_79);
        flavorMap.put("AICHI D3A-1", R.drawable.aichi_d3a1);
        flavorMap.put("SPITFIRE Mk Ia", R.drawable.spitfire_mk_ia);
        flavorMap.put("SPITFIRE Mk II", R.drawable.spitfire_mk_ii);
        flavorMap.put("SPITFIRE Mk V", R.drawable.spitfire_mk_v);
        flavorMap.put("STIRLING Mk I S3", R.drawable.stirling_mk_i_s3);
        flavorMap.put("STUG III-F", R.drawable.stug_iiif);
        flavorMap.put("STUG III-G", R.drawable.stug_iiig);
        flavorMap.put("STUG III G SCHÜRZEN", R.drawable.stug_iii_g_schrzen);
        flavorMap.put("STUG IV", R.drawable.stug_iv);
        flavorMap.put("SHTURMOVIK Il-2M", R.drawable.shturmovik_il2m);
        flavorMap.put("SU 152", R.drawable.su_152);
        flavorMap.put("SU-76M", R.drawable.su76m);
        flavorMap.put("SU-85", R.drawable.su85);
        flavorMap.put("SWORDFISH Mk I", R.drawable.swordfish_mk_i);
        flavorMap.put("SWORDFISH Mk I", R.drawable.swordfish_mk_i_b);
        flavorMap.put("T19 HOWITZER", R.drawable.t19_howitzer);
        flavorMap.put("TAKASAKI REGIMENT", R.drawable.takasaki_regiment);
        flavorMap.put("TBF-1 AVENGER", R.drawable.tbf1_avenger);
        flavorMap.put("TEMPEST Mk V", R.drawable.tempest_mk_v);
        flavorMap.put("TIGER I-H", R.drawable.tiger_ih);
        flavorMap.put("TIGER I-E", R.drawable.tiger_ie);
        flavorMap.put("TOYAMA REGIMENT", R.drawable.toyama_regiment);
        flavorMap.put("TYPE 94 TK", R.drawable.type_94_tk);
        flavorMap.put("TYPE 1 37mm", R.drawable.type_1_37mm);
        flavorMap.put("TYPE 1 Chi-He", R.drawable.type_1_chihe);
        flavorMap.put("TYPE 3 Chi-Nu", R.drawable.type_3_chinu);
        flavorMap.put("TYPE 4 Ho-Ro", R.drawable.type_4_horo);
        flavorMap.put("TYPE 88 AA GUN", R.drawable.type_88_aa_gun);
        flavorMap.put("TYPE 89 Chi-Ro", R.drawable.type_89_chiro);
        flavorMap.put("TYPE 92 105mm FIELD GUN", R.drawable.type_92_105mm_field_gun);
        flavorMap.put("TYPE 93", R.drawable.type_93);
        flavorMap.put("TYPE 95 Ha-Go", R.drawable.type_95_hago);
        flavorMap.put("TYPE 96 AA GUN", R.drawable.type_96_aa_gun);
        flavorMap.put("TYPE 97 Chi-Ha", R.drawable.type_97_chiha);
        flavorMap.put("TYPE 97 SHINHOTO", R.drawable.type_97_shinhoto);
        flavorMap.put("TYPE 98 Ke-Ni", R.drawable.type_98_keni);
        flavorMap.put("TYPHOON Mk IB", R.drawable.typhoon_mk_ib);
        flavorMap.put("T-28", R.drawable.t28);
        flavorMap.put("T-34 1942", R.drawable.t34_1942);
        flavorMap.put("T-34 1942", R.drawable.t34_1942_b);
        flavorMap.put("T-34-85", R.drawable.t3485);
        flavorMap.put("OT-34", R.drawable.ot34);
        flavorMap.put("T-60", R.drawable.t60);
        flavorMap.put("T-70", R.drawable.t70);
        flavorMap.put("T-70", R.drawable.t70_b);
        flavorMap.put("T-80", R.drawable.t80);
        flavorMap.put("USAAF SPITFIRE IX", R.drawable.usaaf_spitfire_ix);
        flavorMap.put("593rd JASCO", R.drawable.m593rd_jasco);
        flavorMap.put("UTSUNOMIYA REGIMENT", R.drawable.utsunomiya_regiment);
        flavorMap.put("VALENTINE Mk III", R.drawable.valentine_mk_iii);
        flavorMap.put("WAKAMATSU REGIMENT", R.drawable.wakamatsu_regiment);
        flavorMap.put("WELSH GUARDS", R.drawable.welsh_guards);
        flavorMap.put("WESPE", R.drawable.wespe);
        flavorMap.put("WIRBELWIND", R.drawable.wirbelwind);
        flavorMap.put("YAK 3", R.drawable.yak_3);
        flavorMap.put("YAK 7", R.drawable.yak_7);
        flavorMap.put("YAK 9", R.drawable.yak_9);
        flavorMap.put("YAMAGATA REGIMENT", R.drawable.yamagata_regiment);
        flavorMap.put("A6M2 ZERO", R.drawable.a6m2_zero);

        try {
            return flavorMap.get(this.name);
        }
		catch (NullPointerException ex) {
            Log.d(TAG, "Card.java: getImage: Null pointer exception when getting the card image. Maybe it's not in the hashmap in the Card class. " + ex);
            return R.mipmap.ic_launcher;
        }

    }

    public int getAttackImage() {
        Map<String, Integer> attackImageMap;
        attackImageMap = new HashMap<>();
        attackImageMap.put("short", R.drawable.attack);
        attackImageMap.put("long", R.drawable.attack_ranged);
        return attackImageMap.get(attackImage);
    }

    public boolean getPassive() {
        return passive;
    }

    public String getCardId() {
        return this.id;
    }

    //Setters

    public void setCountry(String country) {
        String[] countries = {"usa", "britain", "soviet", "france", "germany", "japan", "italy"};
        for (int i = 0; i<7; i++) {
            if (country.equals(countries[i])) {
                this.country = country;
                return;
            }
        }
        Log.d(TAG, "setCountry: Error setting card country. Check the last logged card!");
        this.country = "france";

    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public void setKredits(int kredits) {
        this.kredits = kredits;
    }

    public void setOpCost(int opCost) {
        this.opCost = opCost;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setAttackImage(String attackImage) {
        this.attackImage = attackImage;
    }

    public void setPassive(boolean passive) {
        this.passive = passive;
    }

    public void setCardId(String id) { this.id = id; }

    public int compareTo(Card o) {
        int result = Integer.compare(kredits, o.getKreditsNumber());
        if (result==0) {
            return name.compareToIgnoreCase(o.getName());
        }
        else {
            return result;
        }
    }
}
