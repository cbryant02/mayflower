package mayflower;

import java.awt.event.KeyEvent;

public interface Keyboard 
{
	/** */
	public static final int KEY_ESCAPE          = KeyEvent.VK_ESCAPE;
	/** */
	public static final int KEY_1               = KeyEvent.VK_1;
	/** */
	public static final int KEY_2               = KeyEvent.VK_2;
	/** */
	public static final int KEY_3               = KeyEvent.VK_3;
	/** */
	public static final int KEY_4               = KeyEvent.VK_4;
	/** */
	public static final int KEY_5               = KeyEvent.VK_5;
	/** */
	public static final int KEY_6               = KeyEvent.VK_6;
	/** */
	public static final int KEY_7               = KeyEvent.VK_7;
	/** */
	public static final int KEY_8               = KeyEvent.VK_8;
	/** */
	public static final int KEY_9               = KeyEvent.VK_9;
	/** */
	public static final int KEY_0               = KeyEvent.VK_0;
	/** */
	public static final int KEY_MINUS           = KeyEvent.VK_MINUS; /* - on main keyboard */
	/** */
	public static final int KEY_EQUALS          = KeyEvent.VK_EQUALS;
	/** */
	public static final int KEY_BACK            = KeyEvent.VK_BACK_SPACE; /* backspace */
	/** */
	public static final int KEY_TAB             = KeyEvent.VK_TAB;
	/** */
	public static final int KEY_Q               = KeyEvent.VK_Q;
	/** */
	public static final int KEY_W               = KeyEvent.VK_W;
	/** */
	public static final int KEY_E               = KeyEvent.VK_E;
	/** */
	public static final int KEY_R               = KeyEvent.VK_R;
	/** */
	public static final int KEY_T               = KeyEvent.VK_T;
	/** */
	public static final int KEY_Y               = KeyEvent.VK_Y;
	/** */
	public static final int KEY_U               = KeyEvent.VK_U;
	/** */
	public static final int KEY_I               = KeyEvent.VK_I;
	/** */
	public static final int KEY_O               = KeyEvent.VK_O;
	/** */
	public static final int KEY_P               = KeyEvent.VK_P;
	/** */
	public static final int KEY_LBRACKET        = KeyEvent.VK_BRACELEFT;
	/** */
	public static final int KEY_RBRACKET        = KeyEvent.VK_BRACERIGHT;
	/** */
	public static final int KEY_RETURN          = KeyEvent.VK_ENTER; /* Enter on main keyboard */
	/** */
	public static final int KEY_ENTER           = KeyEvent.VK_ENTER; /* Enter on main keyboard */
	/** */
	public static final int KEY_LCONTROL        = KeyEvent.VK_CONTROL;
	/** */
	public static final int KEY_A               = KeyEvent.VK_A;
	/** */
	public static final int KEY_S               = KeyEvent.VK_S;
	/** */
	public static final int KEY_D               = KeyEvent.VK_D;
	/** */
	public static final int KEY_F               = KeyEvent.VK_F;
	/** */
	public static final int KEY_G               = KeyEvent.VK_G;
	/** */
	public static final int KEY_H               = KeyEvent.VK_H;
	/** */
	public static final int KEY_J               = KeyEvent.VK_J;
	/** */
	public static final int KEY_K               = KeyEvent.VK_K;
	/** */
	public static final int KEY_L               = KeyEvent.VK_L;
	/** */
	public static final int KEY_SEMICOLON       = KeyEvent.VK_SEMICOLON;
	/** */
	public static final int KEY_APOSTROPHE      = KeyEvent.VK_QUOTE;
	/** */
	public static final int KEY_GRAVE           = KeyEvent.VK_DEAD_GRAVE; /* accent grave */
	/** */
	public static final int KEY_LSHIFT          = KeyEvent.VK_SHIFT;
	/** */
	public static final int KEY_BACKSLASH       = KeyEvent.VK_BACK_SLASH;
	/** */
	public static final int KEY_Z               = KeyEvent.VK_Z;
	/** */
	public static final int KEY_X               = KeyEvent.VK_X;
	/** */
	public static final int KEY_C               = KeyEvent.VK_C;
	/** */
	public static final int KEY_V               = KeyEvent.VK_V;
	/** */
	public static final int KEY_B               = KeyEvent.VK_B;
	/** */
	public static final int KEY_N               = KeyEvent.VK_N;
	/** */
	public static final int KEY_M               = KeyEvent.VK_M;
	/** */
	public static final int KEY_COMMA           = KeyEvent.VK_COMMA;
	/** */
	public static final int KEY_PERIOD          = KeyEvent.VK_PERIOD; /* . on main keyboard */
	/** */
	public static final int KEY_SLASH           = KeyEvent.VK_SLASH; /* / on main keyboard */
	/** */
	public static final int KEY_RSHIFT          = KeyEvent.VK_SHIFT;
	/** */
	public static final int KEY_MULTIPLY        = KeyEvent.VK_MULTIPLY; /* * on numeric keypad */
	/** */
	public static final int KEY_LMENU           = KeyEvent.VK_CONTEXT_MENU; /* left Alt */
	/** */
	public static final int KEY_SPACE           = KeyEvent.VK_SPACE;
	/** */
	public static final int KEY_CAPITAL         = KeyEvent.VK_CAPS_LOCK;
	/** */
	public static final int KEY_F1              = KeyEvent.VK_F1;//0x3B;
	/** */
	public static final int KEY_F2              = KeyEvent.VK_F2;//0x3C;
	/** */
	public static final int KEY_F3              = KeyEvent.VK_F3;//0x3D;
	/** */
	public static final int KEY_F4              = KeyEvent.VK_F4;//0x3E;
	/** */
	public static final int KEY_F5              = KeyEvent.VK_F5;//0x3F;
	/** */
	public static final int KEY_F6              = KeyEvent.VK_F6;//0x40;
	/** */
	public static final int KEY_F7              = KeyEvent.VK_F7;//0x41;
	/** */
	public static final int KEY_F8              = KeyEvent.VK_F8;//0x42;
	/** */
	public static final int KEY_F9              = KeyEvent.VK_F9;
	/** */
	public static final int KEY_F10             = KeyEvent.VK_F10;
	/** */
	public static final int KEY_NUMLOCK         = KeyEvent.VK_NUM_LOCK;
	/** */
	public static final int KEY_SCROLL          = KeyEvent.VK_SCROLL_LOCK; /* Scroll Lock */
	/** */
	public static final int KEY_NUMPAD7         = KeyEvent.VK_NUMPAD7;
	/** */
	public static final int KEY_NUMPAD8         = KeyEvent.VK_NUMPAD8;
	/** */
	public static final int KEY_NUMPAD9         = KeyEvent.VK_NUMPAD9;
	/** */
	public static final int KEY_SUBTRACT        = KeyEvent.VK_SUBTRACT; /* - on numeric keypad */
	/** */
	public static final int KEY_NUMPAD4         = KeyEvent.VK_NUMPAD4;
	/** */
	public static final int KEY_NUMPAD5         = KeyEvent.VK_NUMPAD5;
	/** */
	public static final int KEY_NUMPAD6         = KeyEvent.VK_NUMPAD6;
	/** */
	public static final int KEY_ADD             = KeyEvent.VK_ADD; /* + on numeric keypad */
	/** */
	public static final int KEY_NUMPAD1         = KeyEvent.VK_NUMPAD1;
	/** */
	public static final int KEY_NUMPAD2         = KeyEvent.VK_NUMPAD2;
	/** */
	public static final int KEY_NUMPAD3         = KeyEvent.VK_NUMPAD3;
	/** */
	public static final int KEY_NUMPAD0         = KeyEvent.VK_NUMPAD0;
	/** */
	public static final int KEY_DECIMAL         = KeyEvent.VK_PERIOD; /* . on numeric keypad */
	/** */
	public static final int KEY_F11             = KeyEvent.VK_F11;
	/** */
	public static final int KEY_F12             = KeyEvent.VK_F12;
	/** */
	public static final int KEY_F13             = KeyEvent.VK_F13; /*                     (NEC PC98) */
	/** */
	public static final int KEY_F14             = KeyEvent.VK_F14; /*                     (NEC PC98) */
	/** */
	public static final int KEY_F15             = KeyEvent.VK_F15; /*                     (NEC PC98) */
	/** */
	public static final int KEY_NUMPADEQUALS    = KeyEvent.VK_EQUALS; /* = on numeric keypad (NEC PC98) */
	/** */
	public static final int KEY_AT              = KeyEvent.VK_AT; /*                     (NEC PC98) */
	/** */
	public static final int KEY_COLON           = KeyEvent.VK_COLON; /*                     (NEC PC98) */
	/** */
	public static final int KEY_UNDERLINE       = KeyEvent.VK_UNDERSCORE; /*                     (NEC PC98) */
	/** */
	public static final int KEY_NUMPADENTER     = KeyEvent.VK_ENTER; /* Enter on numeric keypad */
	/** */
	public static final int KEY_RCONTROL        = KeyEvent.VK_CONTROL;
	/** */
	public static final int KEY_NUMPADCOMMA     = KeyEvent.VK_COMMA; /* , on numeric keypad (NEC PC98) */
	/** */
	public static final int KEY_DIVIDE          = KeyEvent.VK_DIVIDE; /* / on numeric keypad */
	/** */
	public static final int KEY_RMENU           = KeyEvent.VK_CONTEXT_MENU; /* right Alt */
	/** */
	public static final int KEY_PAUSE           = KeyEvent.VK_PAUSE; /* Pause */
	/** */
	public static final int KEY_HOME            = KeyEvent.VK_HOME;//0xC7; /* Home on arrow keypad */
	/** */
	public static final int KEY_UP              = KeyEvent.VK_UP;//0xC8; /* UpArrow on arrow keypad */
	/** */
	public static final int KEY_PRIOR           = KeyEvent.VK_PAGE_UP; /* PgUp on arrow keypad */
	/** */
	public static final int KEY_LEFT            = KeyEvent.VK_LEFT;//0xCB; /* LeftArrow on arrow keypad */
	/** */
	public static final int KEY_RIGHT           = KeyEvent.VK_RIGHT;//0xCD; /* RightArrow on arrow keypad */
	/** */
	public static final int KEY_END             = KeyEvent.VK_END;//0xCF; /* End on arrow keypad */
	/** */
	public static final int KEY_DOWN            = KeyEvent.VK_DOWN;//0xD0; /* DownArrow on arrow keypad */
	/** */
	public static final int KEY_NEXT            = KeyEvent.VK_PAGE_DOWN; /* PgDn on arrow keypad */
	/** */
	public static final int KEY_INSERT          = KeyEvent.VK_INSERT; /* Insert on arrow keypad */
	/** */
	public static final int KEY_DELETE          = KeyEvent.VK_DELETE;//0xD3; /* Delete on arrow keypad */
	/** */
	public static final int KEY_LWIN            = KeyEvent.VK_WINDOWS; /* Left Windows key */
	/** */
	public static final int KEY_RWIN            = KeyEvent.VK_WINDOWS; /* Right Windows key */
	/** */
	/** A helper for left ALT */
	public static final int KEY_LALT = KEY_LMENU;
	/** A helper for right ALT */
	public static final int KEY_RALT = KEY_RMENU;
}
