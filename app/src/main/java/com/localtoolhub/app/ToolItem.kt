package com.localtoolhub.app

import androidx.annotation.DrawableRes

data class ToolItem(
    val id: String,
    val title: String,
    val url: String,
    @DrawableRes val iconResId: Int
)

object ToolRegistry {
    val items = listOf(
        ToolItem(
            id = "cli_proxy_api",
            title = "CLIProxyApi",
            url = "http://localhost:8317/management.html#/",
            iconResId = R.drawable.ic_tool_terminal
        ),
        ToolItem(
            id = "openclaw",
            title = "Openclaw",
            url = "http://127.0.0.1:18789/overview",
            iconResId = R.drawable.ic_tool_nodes
        ),
        ToolItem(
            id = "settings",
            title = "Settings",
            url = "about:blank",
            iconResId = R.drawable.ic_tool_settings
        ),
        ToolItem(
            id = "about",
            title = "About",
            url = "about:blank",
            iconResId = R.drawable.ic_tool_info
        )
    )
}
