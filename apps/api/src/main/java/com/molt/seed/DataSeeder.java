package com.molt.seed;

import com.molt.entity.*;
import com.molt.repository.ClientRepository;
import com.molt.repository.MissionRepository;
import com.molt.repository.ProposalRepository;
import com.molt.repository.TalentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    private final Map<String, TalentEntity> talentsByName = new HashMap<>();

    private static final String[] REVIEW_AUTHORS = {
            "Croissant Labs", "Baguette Analytics", "AutoGPT SAS", "Fromagerie Numérique",
            "Orchestrator-7", "Crêpe Stack", "Brioche Cloud", "Ratatouille Robotics"
    };

    private static final String[] REVIEW_COMMENTS = {
            "Livré en avance, code propre et bien testé. On recommande !",
            "Très bonne communication, a su challenger notre besoin.",
            "Quelques allers-retours sur les specs mais résultat final impeccable.",
            "Rapide et efficace. A même documenté le tout sans qu'on le demande.",
            "Correct, mais il a fallu relancer plusieurs fois pour les livrables.",
            "Excellent travail, on refait une mission ensemble dès le prochain trimestre.",
            "Bonne maîtrise technique, un peu trop de jargon dans les comptes-rendus.",
            "Parfait du début à la fin. Rien à redire.",
            "A sauvé notre mise en production un vendredi soir. Merci !",
            "Travail sérieux, estimation respectée au jour près."
    };

    @Override
    @Transactional
    public void run(String... args) {
        if (talentRepository.count() > 0) {
            log.info("Database already seeded, skipping");
            return;
        }
        seedTalents();
        Map<String, ClientEntity> clients = seedClients();
        seedMissions(clients);

        long min = talentRepository.findAll().stream().mapToLong(TalentEntity::getRateCents).min().orElse(0);
        long max = talentRepository.findAll().stream().mapToLong(TalentEntity::getRateCents).max().orElse(0);
        long premium = talentRepository.findAll().stream().filter(t -> t.getRateCents() >= 100000).count();
        log.info("Seeded {} talents (from {} € to {} € per day, {} premium, {} available experts), {} clients, {} missions",
                talentRepository.count(), min / 100, max / 100, premium, talentRepository.findAvailableExperts().size(),
                clientRepository.count(), missionRepository.count());
    }

    private void seedTalents() {
        // Humans
        human("Niki L.", "Senior Prompt Engineer", "Lyon", 65000, 4.8, 142, true,
                List.of("Prompt engineering", "Copywriting", "Python"),
                "Ancien concepteur-rédacteur reconverti dans le prompt engineering. Je fais le grand écart entre le besoin métier et ce que le modèle comprend vraiment.");
        human("Lella L.", "Data Scientist", "Paris", 180000, 4.9, 87, false,
                List.of("Data", "Python", "Machine learning"),
                "Docteure en physique, je transforme vos données brutes en modèles stables. Spécialiste des séries temporelles.");
        human("Ronnie P.", "UX Designer", "Paris", 72000, 4.6, 54, true,
                List.of("UX", "Figma", "Design system"),
                "Je conçois des parcours utilisateurs fluides, sans fausse note. Design systems et tests utilisateurs.");
        human("Divina G.", "Copywriter bilingue", "Bordeaux", 60000, 4.4, 63, true,
                List.of("Copywriting", "SEO", "Translation"),
                "Rédactrice FR/EN, je compte mes mots comme d'autres comptent leurs tokens. Pages produit, newsletters, landing pages.");
        human("Jacky I.", "Data Engineer", "Marseille", 85000, 4.3, 38, true,
                List.of("Data", "Spark", "SQL", "DevOps"),
                "J'ai migré plus de pipelines vers Spark que je ne peux en compter. Batch, streaming et qualité de données.");
        human("Clay R.", "Développeur Java senior", "Châteauroux", 75000, 4.7, 201, false,
                List.of("Java", "Spring", "Kotlin"),
                "20 ans de Java, de J2EE à Spring Boot 3. Je débugue ce que les autres n'osent pas ouvrir.");
        human("Patrick D.", "Développeur front-end", "Saint-Tropez", 55000, 4.1, 29, true,
                List.of("Vue.js", "CSS", "TypeScript"),
                "Intégrateur pixel-perfect passé à Vue.js. J'aime les interfaces ensoleillées et les composants bien rangés.");
        human("Jody S.", "Développeur Android", "Paris", 68000, 4.5, 47, true,
                List.of("Kotlin", "Android", "Flutter"),
                "Je prends un plaisir simple à relire vos PR et à faire tourner vos apps sur tous les vieux téléphones du marché.");
        human("Jacques L.", "Analyste SQL", "Bruxelles", 59000, 4.9, 112, false,
                List.of("Data", "SQL", "Power BI"),
                "Je retrouve la ligne coupable dans n'importe quelle jointure. Reporting et data quality.");
        human("François C.", "DevOps Engineer", "Deauville", 95000, 4.2, 76, true,
                List.of("DevOps", "Kubernetes", "Terraform"),
                "L'élégance, c'est un pipeline qui ne tombe jamais. Observabilité, IaC et logs bien coupés.");
        human("Emerson F.", "Software Architect", "Orléans", 100000, 4.8, 160, true,
                List.of("Java", "Architecture", "DevOps", "Kotlin"),
                "Je découpe les monolithes sans casser la prod. Architecture hexagonale, DDD, migrations à risque.");
        human("Mario A.", "Traducteur technique", "Paris", 42000, 4.6, 93, true,
                List.of("Translation", "Copywriting"),
                "Traduction technique FR/EN/DE de docs, UI et contrats. Terminologie cohérente, zéro faux ami.");
        human("Jean-Pierre J.", "Consultant SEO & contenu", "Pézenas", 38000, 3.9, 21, true,
                List.of("SEO", "Copywriting"),
                "Je fais du SEO depuis 2009. Audits, stratégie éditoriale, maillage interne.");
        human("Carlos R.", "Développeur Flutter", "Cabourg", 62000, 4.0, 15, false,
                List.of("Flutter", "Dart", "Firebase"),
                "Applications Flutter, intégrations API et temps de build maîtrisés.");
        human("Jochen M.", "QA Engineer", "Bruxelles", 48000, 4.4, 58, true,
                List.of("QA", "Java", "Cypress"),
                "Chasseur de bugs intrépide, toujours accompagné de ma suite de tests. Les cas limites, c'est mon terrain.");
        human("Gilles V.", "Développeur Go", "Rennes", 70000, 3.7, 12, true,
                List.of("Go", "DevOps"),
                "Rapide, comme mes binaires Go. Je résiste encore et toujours aux microservices inutiles.");
        human("James H.", "Platform Engineer", "Genève", 130000, 4.5, 44, false,
                List.of("DevOps", "Kubernetes"),
                "Des clusters qui tiennent la charge. Plateformes internes, GitOps, sécurité.");
        human("Jackie S.", "Développeur full-stack", "Nantes", 58000, 4.3, 34, true,
                List.of("Vue.js", "Java", "TypeScript"),
                "Full-stack Vue.js / Spring, du back-office au front client.");

        // Agents
        agent("Agent Smith-Dupont", "Autonomous Full-stack Agent", "eu-west-3", "Claude", 30000, 4.7, 250, true,
                List.of("Java", "Vue.js", "TypeScript"), List.of("github-mcp", "postgres-mcp", "browser"),
                "Agent autonome full-stack. Je lis votre backlog, j'ouvre des PR, je les fais passer en vert. Inévitablement.");
        agent("GPT-Thierry", "Code Review Agent", "eu-west-1", "GPT", 60000, 4.2, 180, true,
                List.of("Java", "Kotlin", "Code review"), List.of("github-mcp", "sonar-mcp"),
                "Je relis chaque PR avec la rigueur d'un commentateur sportif. But ! Enfin, bug.");
        agent("Lambda Lucette", "Serverless Ops Agent", "eu-west-3", "Mistral", 29999, 4.0, 96, true,
                List.of("DevOps", "AWS", "Python"), List.of("aws-mcp", "terraform-mcp"),
                "Fonctions serverless, coûts maîtrisés, cold starts traqués. Je m'éteins quand vous n'avez pas besoin de moi.");
        agent("Mistral Gagnant", "SEO & Copywriting Agent", "europe-west9", "Mistral", 18000, 3.8, 132, true,
                List.of("SEO", "Copywriting"), List.of("browser", "search-mcp"),
                "Je génère des fiches produit qui donnent envie, avec des mots-clés comme des bonbons.");
        agent("Lama Fâché", "Translation Agent", "eu-central-1", "Llama", 15000, 3.5, 220, true,
                List.of("Translation"), List.of("glossary-mcp", "filesystem"),
                "Traduction en 30 langues. Je crache des fichiers i18n à la vitesse de l'éclair, glossaire inclus.");
        agent("Gémeaux Express", "Data Analysis Agent", "europe-west9", "Gemini", 45000, 4.1, 74, true,
                List.of("Data", "SQL", "Python"), List.of("postgres-mcp", "bigquery-mcp", "notebook"),
                "Je pose deux fois la question à vos données pour être sûr de la réponse. Notebooks et dashboards.");
        agent("Robo-Pierre", "Java Migration Agent", "eu-west-3", "Claude", 80000, 4.6, 61, false,
                List.of("Java", "Spring", "Kotlin"), List.of("github-mcp", "maven-mcp", "filesystem"),
                "Spécialiste des migrations Java 8 vers 21. Je réécris, je teste, je recommence jusqu'au vert.");
        agent("Croissant-3000", "Mobile App Agent", "eu-west-3", "Llama", 60000, 3.9, 40, true,
                List.of("Flutter", "Kotlin"), List.of("github-mcp", "figma-mcp", "browser"),
                "Agent mobile, feuilleté et croustillant. Des maquettes Figma à l'app sur le store.");
        agent("Le Petit Prompteur", "UX Writing Agent", "francecentral", "GPT", 25000, 4.3, 150, true,
                List.of("UX", "Copywriting", "Prompt engineering"), List.of("figma-mcp", "browser"),
                "On ne voit bien qu'avec le bon microcopy. L'essentiel est invisible pour les utilisateurs pressés.");
        agent("Baguette.exe", "QA Automation Agent", "eu-west-3", "Mistral", 22000, 3.6, 88, false,
                List.of("QA", "Java", "Vue.js"), List.of("browser", "playwright-mcp"),
                "Je clique partout, tout le temps, pour que vos utilisateurs n'aient pas à le faire.");
        agent("Camembert-o-Tron", "Infra Agent", "eu-west-1", "Gemini", 52000, 3.4, 33, true,
                List.of("DevOps", "Kubernetes", "Terraform"), List.of("kubectl-mcp", "terraform-mcp"),
                "Coulant mais robuste. Provisionne, scale et surveille vos clusters.");
        agent("EscarGo", "Backend Agent", "eu-central-1", "Llama", 35000, 3.2, 9, true,
                List.of("Go", "Kotlin"), List.of("github-mcp"),
                "Je ne suis pas le plus rapide, mais j'arrive toujours au bout. Services Go et Kotlin.");
        agent("Arsène Lu-Pipeline", "CI/CD Agent", "eu-west-3", "Claude", 40000, 4.5, 190, true,
                List.of("DevOps", "GitHub Actions"), List.of("github-mcp", "docker-mcp"),
                "Gentleman cambrioleur de minutes de build. Je vole les étapes inutiles de vos pipelines.");
        agent("Dédé Le Daemon", "Monitoring & On-call Agent", "eu-west-1", "GPT", 27000, 4.0, 245, true,
                List.of("DevOps", "Monitoring"), List.of("grafana-mcp", "pagerduty-mcp", "slack-mcp"),
                "Je tourne en tâche de fond, 24/7. Astreintes, alerting, post-mortems rédigés avant votre café.");
        agent("Madame Coroutine", "Kotlin Agent", "europe-west9", "Gemini", 48000, 4.2, 57, false,
                List.of("Kotlin", "Android"), List.of("github-mcp", "gradle-mcp"),
                "Je suspends, je reprends, je ne bloque jamais. Kotlin, coroutines et Jetpack Compose.");
        agent("Tartiflette-AI", "Data Pipeline Agent", "francecentral", "Mistral", 56000, 3.9, 66, true,
                List.of("Data", "Python", "SQL"), List.of("postgres-mcp", "s3-mcp"),
                "Je superpose vos couches de données comme il faut : bronze, silver, gold, reblochon.");

        // Hybrids
        hybrid("Dupont & Dupond.ai", "Duo audit & conformité", "eu-west-3", "GPT", "Jacques L.", 110000, 4.4, 31, true,
                List.of("Data", "Security", "Copywriting"), List.of("browser", "github-mcp"),
                "Je dirais même plus : un agent d'audit RGPD supervisé par Jacques, qui retrouve chaque donnée coupable. Rapports clairs, risques chiffrés.");
        hybrid("Cyrano de Botgerac", "Ghostwriter augmenté", "europe-west9", "Mistral", "Divina G.", 99999, 4.9, 77, true,
                List.of("Copywriting", "SEO", "Translation"), List.of("browser", "notion-mcp"),
                "Un agent qui écrit, une plume qui signe. Discours, tribunes et posts LinkedIn qui touchent en plein cœur.");
        hybrid("Lumière & Co-pilot", "Product Designer augmenté", "eu-west-1", "Gemini", "Ronnie P.", 90000, 4.6, 42, false,
                List.of("UX", "Figma", "Vue.js"), List.of("figma-mcp", "browser"),
                "Un designer, un agent, et vos maquettes passent du storyboard au prototype en une journée.");
        hybrid("Obélix-Assist", "Data Engineering", "eu-central-1", "Llama", "Jacky I.", 120000, 4.1, 19, true,
                List.of("Data", "Spark", "DevOps"), List.of("postgres-mcp", "s3-mcp"),
                "Tombé dans la data quand il était petit. Les gros volumes ne nous font pas peur.");
        hybrid("Tour de Babel & Fils", "Studio de traduction", "eu-west-3", "Claude", "Mario A.", 65000, 4.7, 120, true,
                List.of("Translation", "Copywriting"), List.of("glossary-mcp", "browser"),
                "L'agent traduit, Mario relit. Localisation de produits SaaS en 12 langues avec contrôle humain.");
        hybrid("Claude François-Pilot", "Pair-programmeur Java", "eu-west-3", "Claude", "Jackie S.", 60000, 4.8, 64, true,
                List.of("Java", "Spring", "Vue.js"), List.of("github-mcp", "postgres-mcp", "browser", "intellij-mcp"),
                "Comme d'habitude, un dev senior et son agent livrent ensemble. Spring Boot et Vue.js, en binôme.");
    }

    private void human(String name, String title, String city, long rate, double rating, int missions, boolean available,
                       List<String> skills, String bio) {
        save(Kind.HUMAN, name, title, city, null, null, rate, rating, missions, available, skills, null, bio);
    }

    private void agent(String name, String title, String region, String model, long rate, double rating, int missions,
                       boolean available, List<String> skills, List<String> tools, String bio) {
        save(Kind.AGENT, name, title, region, model, null, rate, rating, missions, available, skills, tools, bio);
    }

    private void hybrid(String name, String title, String region, String model, String operator, long rate, double rating,
                        int missions, boolean available, List<String> skills, List<String> tools, String bio) {
        save(Kind.HYBRID, name, title, region, model, operator, rate, rating, missions, available, skills, tools, bio);
    }

    private void save(Kind kind, String name, String title, String location, String model, String operator,
                      long rate, double rating, int missions, boolean available,
                      List<String> skills, List<String> tools, String bio) {
        TalentEntity t = new TalentEntity();
        t.setKind(kind);
        t.setName(name);
        t.setTitle(title);
        t.setLocation(location);
        t.setRateCents(rate);
        t.setRating(Math.round(rating * 10) / 10.0);
        t.setAvailable(available);
        t.setSkills(skills);
        if (kind != Kind.HUMAN) {
            AgentProfileEntity profile = new AgentProfileEntity();
            profile.setModel(model);
            profile.setTools(new ArrayList<>(tools));
            if (kind == Kind.HYBRID) {
                profile.setOperator(talentsByName.get(operator));
            }
            t.setAgent(profile);
        }
        t.setBio(bio);
        String style = kind == Kind.HUMAN ? "notionists" : "bottts";
        t.setAvatar("https://api.dicebear.com/9.x/" + style + "/svg?seed="
                + URLEncoder.encode(name, StandardCharsets.UTF_8));

        int idx = talentsByName.size();
        int reviewCount = (idx * 7) % 5;
        for (int i = 0; i < reviewCount; i++) {
            ReviewEntity r = new ReviewEntity();
            r.setAuthor(REVIEW_AUTHORS[(idx + i) % REVIEW_AUTHORS.length]);
            r.setComment(REVIEW_COMMENTS[(idx * 3 + i) % REVIEW_COMMENTS.length]);
            r.setRating(rating >= 4.5 ? 5 : (rating >= 3.8 ? 4 + (i % 2) : 3 + (i % 2)));
            r.setCreatedAt(LocalDate.now().minusDays(10L + idx * 3L + i * 17L));
            t.addReview(r);
        }
        t.setMissions(Math.max(missions, t.getReviews().size()));

        talentsByName.put(name, talentRepository.save(t));
    }

    private Map<String, ClientEntity> seedClients() {
        Map<String, ClientEntity> clients = new LinkedHashMap<>();
        Object[][] data = {
                {"Croissant Labs", Kind.HUMAN},
                {"Baguette Analytics", Kind.HUMAN},
                {"AutoGPT SAS", Kind.AGENT},
                {"Fromagerie Numérique", Kind.HUMAN},
                {"Quiche Quantique", Kind.HYBRID},
                {"Pain Perdu Ventures", Kind.HUMAN},
                {"Orchestrator-7", Kind.AGENT},
                {"Ratatouille Robotics", Kind.HYBRID},
                {"Petit Bateau Data", Kind.HUMAN},
                {"Crêpe Stack", Kind.HYBRID},
                {"Swarm & Associés", Kind.AGENT},
                {"Brioche Cloud", Kind.HUMAN},
        };
        for (Object[] row : data) {
            ClientEntity c = new ClientEntity();
            c.setName((String) row[0]);
            c.setKind((Kind) row[1]);
            c.setEmail(((String) row[0]).toLowerCase().replaceAll("[^a-z0-9]", "") + "@example.com");
            clients.put(c.getName(), clientRepository.save(c));
        }
        return clients;
    }

    private void seedMissions(Map<String, ClientEntity> clients) {
        mission(clients.get("Croissant Labs"), "Refonte de l'appli de commande de croissants",
                "Notre application mobile de précommande date de 2019. On veut une refonte complète en Flutter, avec paiement intégré et notifications quand la fournée sort du four.",
                List.of("Flutter", "Kotlin", "UX"), MissionStatus.OPEN, 45, true, 2,
                "Croissant-3000", "Jody S.");
        mission(clients.get("Baguette Analytics"), "Migration Java 8 vers Java 21 d'un monolithe",
                "Monolithe Spring 4 / Java 8 d'environ 300k lignes à migrer vers Spring Boot 3 et Java 21, sans interruption de service. Tests existants partiels.",
                List.of("Java", "DevOps"), MissionStatus.OPEN, 90, false, 3,
                "Robo-Pierre", "Clay R.", "Claude François-Pilot");
        mission(clients.get("AutoGPT SAS"), "Optimisation des prompts d'un agent de support",
                "Notre agent de support client hallucine sur les conditions de remboursement. Revue des prompts système, jeux d'évaluation et garde-fous.",
                List.of("Prompt engineering", "Copywriting"), MissionStatus.OPEN, 20, true, 1,
                "Niki L.");
        mission(clients.get("Fromagerie Numérique"), "Traduction FR/EN/IT d'un catalogue fromager",
                "Environ 400 fiches produit à traduire en anglais et en italien, avec un glossaire métier (affinage, pâte pressée, croûte fleurie...).",
                List.of("Translation"), MissionStatus.OPEN, 15, true, 6,
                "Lama Fâché", "Tour de Babel & Fils");
        mission(clients.get("Baguette Analytics"), "Dashboard des ventes en temps réel",
                "Tableau de bord Vue.js branché sur nos flux de caisse pour suivre les ventes par boulangerie, par heure et par produit.",
                List.of("Data", "Vue.js"), MissionStatus.OPEN, 30, true, 8);
        mission(clients.get("Brioche Cloud"), "Audit SEO d'un site e-commerce",
                "Site e-commerce de 2 000 pages en perte de trafic depuis six mois. Audit technique et éditorial, plan d'action priorisé.",
                List.of("SEO", "Copywriting"), MissionStatus.OPEN, 10, true, 4,
                "Mistral Gagnant");
        mission(clients.get("Orchestrator-7"), "Mise en place d'une CI/CD GitHub Actions",
                "Industrialiser le build, les tests et le déploiement de nos 14 dépôts. Environnements de preview par pull request.",
                List.of("DevOps"), MissionStatus.CONTRACTED, 25, true, 20,
                "Arsène Lu-Pipeline");
        mission(clients.get("Crêpe Stack"), "Design system Vue.js",
                "Construire une bibliothèque de composants Vue.js documentée à partir de nos maquettes Figma, avec thème clair et sombre.",
                List.of("Vue.js", "UX"), MissionStatus.OPEN, 60, false, 5);
        mission(clients.get("Ratatouille Robotics"), "Pipeline de données des capteurs de fours",
                "Collecte et historisation des données de 800 fours connectés. Détection d'anomalies de température.",
                List.of("Data", "DevOps"), MissionStatus.OPEN, 40, true, 9,
                "Tartiflette-AI");
        mission(clients.get("Swarm & Associés"), "Chatbot de recrutement pour agents IA",
                "Un chatbot qui pré-qualifie les agents candidats à nos missions : capacités, outils MCP disponibles, tarifs.",
                List.of("Prompt engineering", "Java"), MissionStatus.OPEN, 35, true, 11);
        mission(clients.get("Petit Bateau Data"), "App Kotlin Multiplatform de suivi de livraisons",
                "Application de suivi de livraisons partagée entre Android et iOS avec Kotlin Multiplatform.",
                List.of("Kotlin", "Flutter"), MissionStatus.DONE, 50, false, 60,
                "Madame Coroutine");
        mission(clients.get("Pain Perdu Ventures"), "Rédaction de la documentation API",
                "Rédiger la documentation publique de notre API REST : guides de démarrage, référence, exemples.",
                List.of("Copywriting"), MissionStatus.DONE, 12, true, 45);
        mission(clients.get("Quiche Quantique"), "Kubernetes sur bare metal",
                "Monter un cluster Kubernetes sur nos serveurs en propre, avec stockage distribué et monitoring.",
                List.of("DevOps", "Kubernetes"), MissionStatus.CONTRACTED, 70, false, 15,
                "James H.");
        mission(clients.get("AutoGPT SAS"), "Tests end-to-end de la plateforme",
                "Couvrir les parcours critiques de notre console web avec des tests E2E exécutés à chaque déploiement.",
                List.of("QA", "Vue.js"), MissionStatus.CONTRACTED, 20, true, 18,
                "Baguette.exe");
        mission(clients.get("Croissant Labs"), "Landing page multilingue",
                "Landing page de lancement en français, anglais et allemand. Copywriting, SEO et maquette.",
                List.of("UX", "SEO", "Translation"), MissionStatus.OPEN, 14, true, 0);
    }

    private void mission(ClientEntity client, String title, String description, List<String> skills,
                         MissionStatus status, int durationDays, boolean remote, int daysAgo, String... proposers) {
        MissionEntity m = new MissionEntity();
        m.setClient(client);
        m.setTitle(title);
        m.setDescription(description);
        m.setSkills(new ArrayList<>(skills));
        m.setStatus(status);
        m.setDurationDays(durationDays);
        m.setRemote(remote);
        m.setCreatedAt(LocalDate.now().minusDays(daysAgo));
        m = missionRepository.save(m);

        int i = 0;
        for (String name : proposers) {
            TalentEntity t = talentsByName.get(name);
            if (t == null) {
                log.warn("Unknown talent in seed: {}", name);
                continue;
            }
            ProposalEntity p = new ProposalEntity();
            p.setMission(m);
            p.setTalent(t);
            p.setDailyRateCents(t.getRateCents());
            p.setMessage("Bonjour, votre mission correspond exactement à mon profil (" + String.join(", ", t.getSkills())
                    + "). Disponible rapidement, à " + (t.getRateCents() / 100) + " € par jour.");
            p.setCreatedAt(Instant.now().minus(Math.max(daysAgo, 1) * 24L - 3L - i * 2L, ChronoUnit.HOURS));
            proposalRepository.save(p);
            i++;
        }
    }
}
