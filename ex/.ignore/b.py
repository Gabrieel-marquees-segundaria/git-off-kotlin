# -*- coding: utf-8 -*-
from kivymd.uix.screen import MDScreen
from kivymd.uix.snackbar import MDSnackbar
from kivymd.uix.label import MDLabel
from kivymd.uix.boxlayout import MDBoxLayout
from kivy.properties import StringProperty, ObjectProperty
from kivy.lang import Builder
from kivy.metrics import dp

# Define the KV string for the base components

BaseScreenKv = """
<BaseLayout>:
    id: main_layout
    orientation: "vertical"
    spacing: dp(16)
    padding: dp(16)

    MDTopAppBar:
        id: toolbar
        title: root.title
        icon: "git"
        left_action_items: [["arrow-left", lambda x: root.callback()],[root.icon_name]] if root.show_back_button else []
        elevation: 3
        md_bg_color: app.theme_cls.primary_color
        specific_text_color: (1, 1, 1, 1)
        font_name: "assets/NotoColorEmoji.ttf"

<BaseScrollView@MDScrollView>:
    id: main_scroll
    bar_width: 0
    do_scroll_x: False

    MDBoxLayout:
        id: scroll_container
        orientation: 'vertical'
        size_hint_y: None
        height: self.minimum_height
        spacing: dp(16)
        padding: dp(16)
        adaptive_height: True

<BaseScreen>:

        
 
"""


class BaseLayout(MDBoxLayout):
    """
    Base layout component with toolbar and navigation callback.

    Properties:
        title (str): Title displayed in the toolbar
        show_back_button (bool): Whether to show the back button
    """

    icon_name = "water-plus"
    title = StringProperty("Title         ")
    show_back_button = StringProperty(True)
    callback_function = ObjectProperty(None)

    def __init__(
        self, callback=None, title="Title           ", show_back_button=True, **kwargs
    ):
        super().__init__(**kwargs)
        self.callback_function = callback
        self.title = title
        self.show_back_button = show_back_button

    def callback(self):
        """Execute the callback function if it's callable."""
        print(f"BaseLayout.callback called for: {self.title}")
        if callable(self.callback_function):
            self.callback_function()

    def set_title(self, title):
        """Update the toolbar title."""
        self.title = title
        if hasattr(self, "ids") and "toolbar" in self.ids:
            self.ids.toolbar.title = title

    def set_callback(self, callback_function):
        """Set the callback function for navigation."""
        self.callback_function = callback_function


class BaseScreen(MDScreen):
    """
    Base screen class with common functionality.

    Properties:
        title (str): Screen title
        home_screen (str): Name of the home screen for navigation

    Usage example:
    ```
    <MyCustomScreen>:
        name: 'custom'
        title: 'My Custom Screen'

        # Content will be automatically wrapped in BaseLayout and BaseScrollView
        MDLabel:
            text: 'Hello World'
            adaptive_height: True
    ```
    """

    title = StringProperty("Base Screen")
    home_screen = StringProperty("home")
    popup = ObjectProperty(None, allownone=True)

    def __init__(self, home_screen="home", **kwargs):
        super().__init__(**kwargs)
        self.home_screen = home_screen

    def on_kv_post(self, base_widget):
        """Called after the KV file is loaded. Set up the layout."""
        super().on_kv_post(base_widget)
        if hasattr(self, "ids") and "main_layout" in self.ids:
            self.ids.main_layout.set_title(self.title)
            self.ids.main_layout.set_callback(self.go_back)

    def on_enter(self):
        pass

    def show_snackbar(self, text, duration=3):
        """
        Display a snackbar with the given text.

        Args:
            text (str): Text to display
            duration (float): Duration in seconds
        """
        snackbar = MDSnackbar(
            MDLabel(text=text, theme_text_color="Custom", text_color=(1, 1, 1, 1)),
            y=dp(24),
            pos_hint={"center_x": 0.5},
            size_hint_x=0.9,
            duration=duration,
        )
        snackbar.open()

    def go_back(self):
        """Navigate back to the home screen or previous screen."""
        self.navigate_to(self.home_screen)

    def navigate_to(self, screen_name):
        """
        Navigate to a specific screen.

        Args:
            screen_name (str): Name of the screen to navigate to
        """
        screen_manager = self.parent
        if screen_manager and hasattr(screen_manager, "current"):
            screen_manager.current = screen_name
        else:
            print(f"Warning: Could not navigate to {screen_name}")

    def set_popup(self, popup):
        """
        Set a popup reference for this screen.

        Args:
            popup: Popup widget reference

        Returns:
            self: Returns self for method chaining
        """
        self.popup = popup
        return self

    def add_content_widget(self, widget):
        """
        Add a widget to the scrollable content area.

        Args:
            widget: Widget to add to the content area
        """
        if hasattr(self, "ids") and "main_scroll" in self.ids:
            scroll_view = self.ids.main_scroll
            if hasattr(scroll_view, "ids") and "scroll_container" in scroll_view.ids:
                scroll_view.ids.scroll_container.add_widget(widget)
            else:
                print("Warning: scroll_container not found")
        else:
            print("Warning: main_scroll not found")

    def clear_content(self):
        """Clear all widgets from the scrollable content area."""
        if hasattr(self, "ids") and "main_scroll" in self.ids:
            scroll_view = self.ids.main_scroll
            if hasattr(scroll_view, "ids") and "scroll_container" in scroll_view.ids:
                scroll_view.ids.scroll_container.clear_widgets()

    def on_title(self, instance, value):
        """Update the layout title when the screen title changes."""
        if hasattr(self, "ids") and "main_layout" in self.ids:
            self.ids.main_layout.set_title(value)


# Load the KV string when the module is imported
Builder.load_string(BaseScreenKv)


# Example usage class
class ExampleScreen(BaseScreen):
    """Example implementation of BaseScreen."""

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.title = "Example Screen"

    def on_enter(self):
        """Called when the screen is entered."""
        super().on_enter()
        # Add some example content
        example_label = MDLabel(
            text="This is an example of BaseScreen usage!",
            adaptive_height=True,
            theme_text_color="Primary",
        )
        self.add_content_widget(example_label)
