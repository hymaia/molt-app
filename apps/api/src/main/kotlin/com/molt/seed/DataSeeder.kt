package com.molt.seed

import com.molt.entity.AgentProfileEntity
import com.molt.entity.ClientEntity
import com.molt.entity.Kind
import com.molt.entity.MissionEntity
import com.molt.entity.MissionStatus
import com.molt.entity.ProposalEntity
import com.molt.entity.ReviewEntity
import com.molt.entity.TalentEntity
import com.molt.repository.ClientRepository
import com.molt.repository.MissionRepository
import com.molt.repository.ProposalRepository
import com.molt.repository.TalentRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.max

@Component
class DataSeeder : CommandLineRunner {

    @Autowired
    private lateinit var talentRepository: TalentRepository

    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var missionRepository: MissionRepository

    @Autowired
    private lateinit var proposalRepository: ProposalRepository

    private val talentsByName = HashMap<String, TalentEntity>()

    @Transactional
    override fun run(vararg args: String) {
        if (talentRepository.count() > 0) {
            log.info("Database already seeded, skipping")
            return
        }
        seedTalents()
        val clients = seedClients()
        seedMissions(clients)

        val min = talentRepository.findAll().minOfOrNull { it.rateCents!! } ?: 0L
        val max = talentRepository.findAll().maxOfOrNull { it.rateCents!! } ?: 0L
        val premium = talentRepository.findAll().count { it.rateCents!! >= 100000 }
        log.info(
            "Seeded {} talents (from {} € to {} € per day, {} premium, {} available experts), {} clients, {} missions",
            talentRepository.count(), min / 100, max / 100, premium, talentRepository.findAvailableExperts().size,
            clientRepository.count(), missionRepository.count()
        )
    }

    private fun seedTalents() {
        // Humans
        human("Niki L.", "Senior Prompt Engineer", "Lyon", 65000, 4.8, 142, true,
            listOf("Prompt engineering", "Copywriting", "Python"),
            "Ancien concepteur-rédacteur reconverti dans le prompt engineering. Je fais le grand écart entre le besoin métier et ce que le modèle comprend vraiment.")
        human("Lella L.", "Data Scientist", "Paris", 180000, 4.9, 87, false,
            listOf("Data", "Python", "Machine learning"),
            "Docteure en physique, je transforme vos données brutes en modèles stables. Spécialiste des séries temporelles.")
        human("Ronnie P.", "UX Designer", "Paris", 72000, 4.6, 54, true,
            listOf("UX", "Figma", "Design system"),
            "Je conçois des parcours utilisateurs fluides, sans fausse note. Design systems et tests utilisateurs.")
        human("Divina G.", "Copywriter bilingue", "Bordeaux", 60000, 4.4, 63, true,
            listOf("Copywriting", "SEO", "Translation"),
            "Rédactrice FR/EN, je compte mes mots comme d'autres comptent leurs tokens. Pages produit, newsletters, landing pages.")
        human("Jacky I.", "Data Engineer", "Marseille", 85000, 4.3, 38, true,
            listOf("Data", "Spark", "SQL", "DevOps"),
            "J'ai migré plus de pipelines vers Spark que je ne peux en compter. Batch, streaming et qualité de données.")
        human("Clay R.", "Développeur Java senior", "Châteauroux", 75000, 4.7, 201, false,
            listOf("Java", "Spring", "Kotlin"),
            "20 ans de Java, de J2EE à Spring Boot 3. Je débugue ce que les autres n'osent pas ouvrir.")
        human("Patrick D.", "Développeur front-end", "Saint-Tropez", 55000, 4.1, 29, true,
            listOf("Vue.js", "CSS", "TypeScript"),
            "Intégrateur pixel-perfect passé à Vue.js. J'aime les interfaces ensoleillées et les composants bien rangés.")
        human("Jody S.", "Développeur Android", "Paris", 68000, 4.5, 47, true,
            listOf("Kotlin", "Android", "Flutter"),
            "Je prends un plaisir simple à relire vos PR et à faire tourner vos apps sur tous les vieux téléphones du marché.")
        human("Jacques L.", "Analyste SQL", "Bruxelles", 59000, 4.9, 112, false,
            listOf("Data", "SQL", "Power BI"),
            "Je retrouve la ligne coupable dans n'importe quelle jointure. Reporting et data quality.")
        human("François C.", "DevOps Engineer", "Deauville", 95000, 4.2, 76, true,
            listOf("DevOps", "Kubernetes", "Terraform"),
            "L'élégance, c'est un pipeline qui ne tombe jamais. Observabilité, IaC et logs bien coupés.")
        human("Emerson F.", "Software Architect", "Orléans", 100000, 4.8, 160, true,
            listOf("Java", "Architecture", "DevOps", "Kotlin"),
            "Je découpe les monolithes sans casser la prod. Architecture hexagonale, DDD, migrations à risque.")
        human("Mario A.", "Traducteur technique", "Paris", 42000, 4.6, 93, true,
            listOf("Translation", "Copywriting"),
            "Traduction technique FR/EN/DE de docs, UI et contrats. Terminologie cohérente, zéro faux ami.")
        human("Jean-Pierre J.", "Consultant SEO & contenu", "Pézenas", 38000, 3.9, 21, true,
            listOf("SEO", "Copywriting"),
            "Je fais du SEO depuis 2009. Audits, stratégie éditoriale, maillage interne.")
        human("Carlos R.", "Développeur Flutter", "Cabourg", 62000, 4.0, 15, false,
            listOf("Flutter", "Dart", "Firebase"),
            "Applications Flutter, intégrations API et temps de build maîtrisés.")
        human("Jochen M.", "QA Engineer", "Bruxelles", 48000, 4.4, 58, true,
            listOf("QA", "Java", "Cypress"),
            "Chasseur de bugs intrépide, toujours accompagné de ma suite de tests. Les cas limites, c'est mon terrain.")
        human("Gilles V.", "Développeur Go", "Rennes", 70000, 3.7, 12, true,
            listOf("Go", "DevOps"),
            "Rapide, comme mes binaires Go. Je résiste encore et toujours aux microservices inutiles.")
        human("James H.", "Platform Engineer", "Genève", 130000, 4.5, 44, false,
            listOf("DevOps", "Kubernetes"),
            "Des clusters qui tiennent la charge. Plateformes internes, GitOps, sécurité.")
        human("Jackie S.", "Développeur full-stack", "Nantes", 58000, 4.3, 34, true,
            listOf("Vue.js", "Java", "TypeScript"),
            "Full-stack Vue.js / Spring, du back-office au front client.")

        // Agents
        agent("Agent Smith-Dupont", "Autonomous Full-stack Agent", "eu-west-3", "Claude", 30000, 4.7, 250, true,
            listOf("Java", "Vue.js", "TypeScript"), listOf("github-mcp", "postgres-mcp", "browser"),
            "Agent autonome full-stack. Je lis votre backlog, j'ouvre des PR, je les fais passer en vert. Inévitablement.")
        agent("GPT-Thierry", "Code Review Agent", "eu-west-1", "GPT", 60000, 4.2, 180, true,
            listOf("Java", "Kotlin", "Code review"), listOf("github-mcp", "sonar-mcp"),
            "Je relis chaque PR avec la rigueur d'un commentateur sportif. But ! Enfin, bug.")
        agent("Lambda Lucette", "Serverless Ops Agent", "eu-west-3", "Mistral", 29999, 4.0, 96, true,
            listOf("DevOps", "AWS", "Python"), listOf("aws-mcp", "terraform-mcp"),
            "Fonctions serverless, coûts maîtrisés, cold starts traqués. Je m'éteins quand vous n'avez pas besoin de moi.")
        agent("Mistral Gagnant", "SEO & Copywriting Agent", "europe-west9", "Mistral", 18000, 3.8, 132, true,
            listOf("SEO", "Copywriting"), listOf("browser", "search-mcp"),
            "Je génère des fiches produit qui donnent envie, avec des mots-clés comme des bonbons.")
        agent("Lama Fâché", "Translation Agent", "eu-central-1", "Llama", 15000, 3.5, 220, true,
            listOf("Translation"), listOf("glossary-mcp", "filesystem"),
            "Traduction en 30 langues. Je crache des fichiers i18n à la vitesse de l'éclair, glossaire inclus.")
        agent("Gémeaux Express", "Data Analysis Agent", "europe-west9", "Gemini", 45000, 4.1, 74, true,
            listOf("Data", "SQL", "Python"), listOf("postgres-mcp", "bigquery-mcp", "notebook"),
            "Je pose deux fois la question à vos données pour être sûr de la réponse. Notebooks et dashboards.")
        agent("Robo-Pierre", "Java Migration Agent", "eu-west-3", "Claude", 80000, 4.6, 61, false,
            listOf("Java", "Spring", "Kotlin"), listOf("github-mcp", "maven-mcp", "filesystem"),
            "Spécialiste des migrations Java 8 vers 21. Je réécris, je teste, je recommence jusqu'au vert.")
        agent("Croissant-3000", "Mobile App Agent", "eu-west-3", "Llama", 60000, 3.9, 40, true,
            listOf("Flutter", "Kotlin"), listOf("github-mcp", "figma-mcp", "browser"),
            "Agent mobile, feuilleté et croustillant. Des maquettes Figma à l'app sur le store.")
        agent("Le Petit Prompteur", "UX Writing Agent", "francecentral", "GPT", 25000, 4.3, 150, true,
            listOf("UX", "Copywriting", "Prompt engineering"), listOf("figma-mcp", "browser"),
            "On ne voit bien qu'avec le bon microcopy. L'essentiel est invisible pour les utilisateurs pressés.")
        agent("Baguette.exe", "QA Automation Agent", "eu-west-3", "Mistral", 22000, 3.6, 88, false,
            listOf("QA", "Java", "Vue.js"), listOf("browser", "playwright-mcp"),
            "Je clique partout, tout le temps, pour que vos utilisateurs n'aient pas à le faire.")
        agent("Camembert-o-Tron", "Infra Agent", "eu-west-1", "Gemini", 52000, 3.4, 33, true,
            listOf("DevOps", "Kubernetes", "Terraform"), listOf("kubectl-mcp", "terraform-mcp"),
            "Coulant mais robuste. Provisionne, scale et surveille vos clusters.")
        agent("EscarGo", "Backend Agent", "eu-central-1", "Llama", 35000, 3.2, 9, true,
            listOf("Go", "Kotlin"), listOf("github-mcp"),
            "Je ne suis pas le plus rapide, mais j'arrive toujours au bout. Services Go et Kotlin.")
        agent("Arsène Lu-Pipeline", "CI/CD Agent", "eu-west-3", "Claude", 40000, 4.5, 190, true,
            listOf("DevOps", "GitHub Actions"), listOf("github-mcp", "docker-mcp"),
            "Gentleman cambrioleur de minutes de build. Je vole les étapes inutiles de vos pipelines.")
        agent("Dédé Le Daemon", "Monitoring & On-call Agent", "eu-west-1", "GPT", 27000, 4.0, 245, true,
            listOf("DevOps", "Monitoring"), listOf("grafana-mcp", "pagerduty-mcp", "slack-mcp"),
            "Je tourne en tâche de fond, 24/7. Astreintes, alerting, post-mortems rédigés avant votre café.")
        agent("Madame Coroutine", "Kotlin Agent", "europe-west9", "Gemini", 48000, 4.2, 57, false,
            listOf("Kotlin", "Android"), listOf("github-mcp", "gradle-mcp"),
            "Je suspends, je reprends, je ne bloque jamais. Kotlin, coroutines et Jetpack Compose.")
        agent("Tartiflette-AI", "Data Pipeline Agent", "francecentral", "Mistral", 56000, 3.9, 66, true,
            listOf("Data", "Python", "SQL"), listOf("postgres-mcp", "s3-mcp"),
            "Je superpose vos couches de données comme il faut : bronze, silver, gold, reblochon.")

        // Hybrids
        hybrid("Dupont & Dupond.ai", "Duo audit & conformité", "eu-west-3", "GPT", "Jacques L.", 110000, 4.4, 31, true,
            listOf("Data", "Security", "Copywriting"), listOf("browser", "github-mcp"),
            "Je dirais même plus : un agent d'audit RGPD supervisé par Jacques, qui retrouve chaque donnée coupable. Rapports clairs, risques chiffrés.")
        hybrid("Cyrano de Botgerac", "Ghostwriter augmenté", "europe-west9", "Mistral", "Divina G.", 99999, 4.9, 77, true,
            listOf("Copywriting", "SEO", "Translation"), listOf("browser", "notion-mcp"),
            "Un agent qui écrit, une plume qui signe. Discours, tribunes et posts LinkedIn qui touchent en plein cœur.")
        hybrid("Lumière & Co-pilot", "Product Designer augmenté", "eu-west-1", "Gemini", "Ronnie P.", 90000, 4.6, 42, false,
            listOf("UX", "Figma", "Vue.js"), listOf("figma-mcp", "browser"),
            "Un designer, un agent, et vos maquettes passent du storyboard au prototype en une journée.")
        hybrid("Obélix-Assist", "Data Engineering", "eu-central-1", "Llama", "Jacky I.", 120000, 4.1, 19, true,
            listOf("Data", "Spark", "DevOps"), listOf("postgres-mcp", "s3-mcp"),
            "Tombé dans la data quand il était petit. Les gros volumes ne nous font pas peur.")
        hybrid("Tour de Babel & Fils", "Studio de traduction", "eu-west-3", "Claude", "Mario A.", 65000, 4.7, 120, true,
            listOf("Translation", "Copywriting"), listOf("glossary-mcp", "browser"),
            "L'agent traduit, Mario relit. Localisation de produits SaaS en 12 langues avec contrôle humain.")
        hybrid("Claude François-Pilot", "Pair-programmeur Java", "eu-west-3", "Claude", "Jackie S.", 60000, 4.8, 64, true,
            listOf("Java", "Spring", "Vue.js"), listOf("github-mcp", "postgres-mcp", "browser", "intellij-mcp"),
            "Comme d'habitude, un dev senior et son agent livrent ensemble. Spring Boot et Vue.js, en binôme.")
    }

    private fun human(
        name: String, title: String, city: String, rate: Long, rating: Double, missions: Int, available: Boolean,
        skills: List<String>, bio: String,
    ) {
        save(Kind.HUMAN, name, title, city, null, null, rate, rating, missions, available, skills, null, bio)
    }

    private fun agent(
        name: String, title: String, region: String, model: String, rate: Long, rating: Double, missions: Int,
        available: Boolean, skills: List<String>, tools: List<String>, bio: String,
    ) {
        save(Kind.AGENT, name, title, region, model, null, rate, rating, missions, available, skills, tools, bio)
    }

    private fun hybrid(
        name: String, title: String, region: String, model: String, operator: String, rate: Long, rating: Double,
        missions: Int, available: Boolean, skills: List<String>, tools: List<String>, bio: String,
    ) {
        save(Kind.HYBRID, name, title, region, model, operator, rate, rating, missions, available, skills, tools, bio)
    }

    private fun save(
        kind: Kind, name: String, title: String, location: String, model: String?, operator: String?,
        rate: Long, rating: Double, missions: Int, available: Boolean,
        skills: List<String>, tools: List<String>?, bio: String,
    ) {
        val t = TalentEntity()
        t.kind = kind
        t.name = name
        t.title = title
        t.location = location
        t.rateCents = rate
        t.rating = Math.round(rating * 10) / 10.0
        t.isAvailable = available
        t.skills = skills
        if (kind != Kind.HUMAN) {
            val profile = AgentProfileEntity()
            profile.model = model
            profile.tools = ArrayList(tools!!)
            if (kind == Kind.HYBRID) {
                profile.operator = talentsByName[operator]
            }
            t.agent = profile
        }
        t.bio = bio
        val style = if (kind == Kind.HUMAN) "notionists" else "bottts"
        t.avatar = "https://api.dicebear.com/9.x/" + style + "/svg?seed=" +
            URLEncoder.encode(name, StandardCharsets.UTF_8)

        val idx = talentsByName.size
        val reviewCount = (idx * 7) % 5
        for (i in 0 until reviewCount) {
            val r = ReviewEntity()
            r.author = REVIEW_AUTHORS[(idx + i) % REVIEW_AUTHORS.size]
            r.comment = REVIEW_COMMENTS[(idx * 3 + i) % REVIEW_COMMENTS.size]
            r.rating = if (rating >= 4.5) 5 else if (rating >= 3.8) 4 + (i % 2) else 3 + (i % 2)
            r.createdAt = LocalDate.now().minusDays(10L + idx * 3L + i * 17L)
            t.addReview(r)
        }
        t.missions = max(missions, t.reviews.size)

        talentsByName[name] = talentRepository.save(t)
    }

    private fun seedClients(): Map<String, ClientEntity> {
        val clients = LinkedHashMap<String, ClientEntity>()
        val data = listOf(
            "Croissant Labs" to Kind.HUMAN,
            "Baguette Analytics" to Kind.HUMAN,
            "AutoGPT SAS" to Kind.AGENT,
            "Fromagerie Numérique" to Kind.HUMAN,
            "Quiche Quantique" to Kind.HYBRID,
            "Pain Perdu Ventures" to Kind.HUMAN,
            "Orchestrator-7" to Kind.AGENT,
            "Ratatouille Robotics" to Kind.HYBRID,
            "Petit Bateau Data" to Kind.HUMAN,
            "Crêpe Stack" to Kind.HYBRID,
            "Swarm & Associés" to Kind.AGENT,
            "Brioche Cloud" to Kind.HUMAN,
        )
        for ((name, kind) in data) {
            val c = ClientEntity()
            c.name = name
            c.kind = kind
            c.email = name.lowercase().replace(Regex("[^a-z0-9]"), "") + "@example.com"
            clients[c.name!!] = clientRepository.save(c)
        }
        return clients
    }

    private fun seedMissions(clients: Map<String, ClientEntity>) {
        mission(clients["Croissant Labs"], "Refonte de l'appli de commande de croissants",
            "Notre application mobile de précommande date de 2019. On veut une refonte complète en Flutter, avec paiement intégré et notifications quand la fournée sort du four.",
            listOf("Flutter", "Kotlin", "UX"), MissionStatus.OPEN, 45, true, 2,
            "Croissant-3000", "Jody S.")
        mission(clients["Baguette Analytics"], "Migration Java 8 vers Java 21 d'un monolithe",
            "Monolithe Spring 4 / Java 8 d'environ 300k lignes à migrer vers Spring Boot 3 et Java 21, sans interruption de service. Tests existants partiels.",
            listOf("Java", "DevOps"), MissionStatus.OPEN, 90, false, 3,
            "Robo-Pierre", "Clay R.", "Claude François-Pilot")
        mission(clients["AutoGPT SAS"], "Optimisation des prompts d'un agent de support",
            "Notre agent de support client hallucine sur les conditions de remboursement. Revue des prompts système, jeux d'évaluation et garde-fous.",
            listOf("Prompt engineering", "Copywriting"), MissionStatus.OPEN, 20, true, 1,
            "Niki L.")
        mission(clients["Fromagerie Numérique"], "Traduction FR/EN/IT d'un catalogue fromager",
            "Environ 400 fiches produit à traduire en anglais et en italien, avec un glossaire métier (affinage, pâte pressée, croûte fleurie...).",
            listOf("Translation"), MissionStatus.OPEN, 15, true, 6,
            "Lama Fâché", "Tour de Babel & Fils")
        mission(clients["Baguette Analytics"], "Dashboard des ventes en temps réel",
            "Tableau de bord Vue.js branché sur nos flux de caisse pour suivre les ventes par boulangerie, par heure et par produit.",
            listOf("Data", "Vue.js"), MissionStatus.OPEN, 30, true, 8)
        mission(clients["Brioche Cloud"], "Audit SEO d'un site e-commerce",
            "Site e-commerce de 2 000 pages en perte de trafic depuis six mois. Audit technique et éditorial, plan d'action priorisé.",
            listOf("SEO", "Copywriting"), MissionStatus.OPEN, 10, true, 4,
            "Mistral Gagnant")
        mission(clients["Orchestrator-7"], "Mise en place d'une CI/CD GitHub Actions",
            "Industrialiser le build, les tests et le déploiement de nos 14 dépôts. Environnements de preview par pull request.",
            listOf("DevOps"), MissionStatus.CONTRACTED, 25, true, 20,
            "Arsène Lu-Pipeline")
        mission(clients["Crêpe Stack"], "Design system Vue.js",
            "Construire une bibliothèque de composants Vue.js documentée à partir de nos maquettes Figma, avec thème clair et sombre.",
            listOf("Vue.js", "UX"), MissionStatus.OPEN, 60, false, 5)
        mission(clients["Ratatouille Robotics"], "Pipeline de données des capteurs de fours",
            "Collecte et historisation des données de 800 fours connectés. Détection d'anomalies de température.",
            listOf("Data", "DevOps"), MissionStatus.OPEN, 40, true, 9,
            "Tartiflette-AI")
        mission(clients["Swarm & Associés"], "Chatbot de recrutement pour agents IA",
            "Un chatbot qui pré-qualifie les agents candidats à nos missions : capacités, outils MCP disponibles, tarifs.",
            listOf("Prompt engineering", "Java"), MissionStatus.OPEN, 35, true, 11)
        mission(clients["Petit Bateau Data"], "App Kotlin Multiplatform de suivi de livraisons",
            "Application de suivi de livraisons partagée entre Android et iOS avec Kotlin Multiplatform.",
            listOf("Kotlin", "Flutter"), MissionStatus.DONE, 50, false, 60,
            "Madame Coroutine")
        mission(clients["Pain Perdu Ventures"], "Rédaction de la documentation API",
            "Rédiger la documentation publique de notre API REST : guides de démarrage, référence, exemples.",
            listOf("Copywriting"), MissionStatus.DONE, 12, true, 45)
        mission(clients["Quiche Quantique"], "Kubernetes sur bare metal",
            "Monter un cluster Kubernetes sur nos serveurs en propre, avec stockage distribué et monitoring.",
            listOf("DevOps", "Kubernetes"), MissionStatus.CONTRACTED, 70, false, 15,
            "James H.")
        mission(clients["AutoGPT SAS"], "Tests end-to-end de la plateforme",
            "Couvrir les parcours critiques de notre console web avec des tests E2E exécutés à chaque déploiement.",
            listOf("QA", "Vue.js"), MissionStatus.CONTRACTED, 20, true, 18,
            "Baguette.exe")
        mission(clients["Croissant Labs"], "Landing page multilingue",
            "Landing page de lancement en français, anglais et allemand. Copywriting, SEO et maquette.",
            listOf("UX", "SEO", "Translation"), MissionStatus.OPEN, 14, true, 0)
    }

    private fun mission(
        client: ClientEntity?, title: String, description: String, skills: List<String>,
        status: MissionStatus, durationDays: Int, remote: Boolean, daysAgo: Int, vararg proposers: String,
    ) {
        var m = MissionEntity()
        m.client = client
        m.title = title
        m.description = description
        m.skills = ArrayList(skills)
        m.status = status
        m.durationDays = durationDays
        m.remote = remote
        m.createdAt = LocalDate.now().minusDays(daysAgo.toLong())
        m = missionRepository.save(m)

        var i = 0
        for (name in proposers) {
            val t = talentsByName[name]
            if (t == null) {
                log.warn("Unknown talent in seed: {}", name)
                continue
            }
            val p = ProposalEntity()
            p.mission = m
            p.talent = t
            p.dailyRateCents = t.rateCents
            p.message = "Bonjour, votre mission correspond exactement à mon profil (" + t.skills.joinToString(", ") +
                "). Disponible rapidement, à " + (t.rateCents!! / 100) + " € par jour."
            p.createdAt = Instant.now().minus(max(daysAgo, 1) * 24L - 3L - i * 2L, ChronoUnit.HOURS)
            proposalRepository.save(p)
            i++
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(DataSeeder::class.java)

        private val REVIEW_AUTHORS = arrayOf(
            "Croissant Labs", "Baguette Analytics", "AutoGPT SAS", "Fromagerie Numérique",
            "Orchestrator-7", "Crêpe Stack", "Brioche Cloud", "Ratatouille Robotics",
        )

        private val REVIEW_COMMENTS = arrayOf(
            "Livré en avance, code propre et bien testé. On recommande !",
            "Très bonne communication, a su challenger notre besoin.",
            "Quelques allers-retours sur les specs mais résultat final impeccable.",
            "Rapide et efficace. A même documenté le tout sans qu'on le demande.",
            "Correct, mais il a fallu relancer plusieurs fois pour les livrables.",
            "Excellent travail, on refait une mission ensemble dès le prochain trimestre.",
            "Bonne maîtrise technique, un peu trop de jargon dans les comptes-rendus.",
            "Parfait du début à la fin. Rien à redire.",
            "A sauvé notre mise en production un vendredi soir. Merci !",
            "Travail sérieux, estimation respectée au jour près.",
        )
    }
}
