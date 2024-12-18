import { createContext, useContext, useState } from "react";

const PopupContext = createContext();

export const PopupProvider = ({ children }) => {
    const [isFriendsPopupOpen, setFriendsPopupOpen] = useState(false);

    const openFriendsPopup = () => setFriendsPopupOpen(true);
    const closeFriendsPopup = () => setFriendsPopupOpen(false);
    
    return (
        <PopupContext.Provider value={{ isFriendsPopupOpen, openFriendsPopup, closeFriendsPopup }}>
            { children }
        </PopupContext.Provider>
    );
};

export const usePopup = () => useContext(PopupContext);