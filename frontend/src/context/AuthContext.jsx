// import React, { createContext, useState, useContext } from 'react';

// // Create the AuthContext
// const AuthContext = createContext();

// // AuthProvider component to wrap your app
// export const AuthProvider = ({ children }) => {
//     const [userID, setUserID] = useState(null);

//     return (
//         <AuthContext.Provider value={{ userID, setUserID }}>
//             {children}
//         </AuthContext.Provider>
//     );
// };

// // Custom hook to use the AuthContext
// export const useAuth = () => {
//     return useContext(AuthContext);
// };