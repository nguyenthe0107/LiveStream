package olmo.wellness.android.ui.livestream.report

enum class ReportType(val value: String) {
    Minor_Report("Minor safety"),
    Dangerous_Report("Dangerous acts and challenges"),
    Suicde_Report("Suicde seft ham and disordered eating"),
    Adult_nudity_Report("Adult nudity and sexual activities"),
    Bullying_Report("Bullying and harassment"),
    Hateful_behavior_Report("Hateful behavior"),
    Violent_behavior_Report("Violent extremmism"),
    Spam_Report("Spam and fake engagement"),
    Harmful_Report("Harmful misinformation"),
    Illegal_Report("Illegal activities and regulated goods"),
    Violent_Graphic_Report("Violent and graphic content"),
    Intellectual_property_Report("Intellectual property infringement"),
    Other_Report("other report");

    companion object {
        operator fun invoke(rawValue: String) = values().find { it.name == rawValue } ?: ""
    }
}