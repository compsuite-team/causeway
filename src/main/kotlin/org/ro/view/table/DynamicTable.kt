package org.ro.view.table

import org.ro.core.event.LogEntry
import org.ro.core.model.MemberExposer
import pl.treksoft.kvision.dropdown.DropDown
import pl.treksoft.kvision.html.Link
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.tabulator.ColumnDefinition
import pl.treksoft.kvision.tabulator.Layout
import pl.treksoft.kvision.tabulator.Tabulator
import pl.treksoft.kvision.tabulator.Tabulator.Companion.tabulator
import pl.treksoft.kvision.tabulator.TabulatorOptions
import pl.treksoft.kvision.utils.px

/**
 * access attributes from dynamic (JS) objects with varying
 * - numbers of attributes
 * - attribute types (can only be determined at runtime) and
 * - accessor names are not fixed
 */
class DynamicTable(
        val model: List<MemberExposer>,
        val columns: List<ColumnDefinition<MemberExposer>>) : VPanel() {

    init {
        HPanel(
                FlexWrap.NOWRAP,
                alignItems = FlexAlignItems.CENTER,
                spacing = 20) {
            padding = 10.px
        }

        val options = TabulatorOptions(
                height = "calc(100vh - 250px)",
                layout = Layout.FITCOLUMNS,
                columns = columns,
                persistenceMode = false
        )

        tabulator(model, options = options) {
            marginTop = 0.px
            marginBottom = 0.px
            setEventListener<Tabulator<MemberExposer>> {
                tabulatorRowClick = {
                }
            }
        }

    }

    fun build(logEntry: LogEntry): DropDown {
        val menu = buildMenuEntry("Action(s) ...", iconName = "fa-ellipsis-h")
        val link = Link(I18n.tr("Details"), icon = "fa-server").onClick {
            console.log("[ActionMenu.build] $logEntry")
        }
        menu.add(link)
        return menu
    }

    private fun buildMenuEntry(title: String, iconName: String? = null): DropDown {
        val label = I18n.tr(title)
        val icon = iconName ?: "fa-bolt"
        return DropDown(label, icon = icon, forNavbar = true)
    }

}
