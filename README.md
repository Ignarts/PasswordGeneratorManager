# Cardea Passkeeper

## Overview
Cardea Passkeeper is a secure and efficient application designed to help users generate, store, and manage their passwords locally with encryption. Built with React and Electron, it provides an intuitive and user-friendly interface while ensuring the highest level of security by leveraging encryption techniques and a master password authentication system.

## Features
- **Secure Password Generation:** Generate strong and secure passwords for various services.
- **Encrypted Storage:** All passwords are stored in an encrypted `passwords.json` file, accessible only with the master password.
- **User Authentication:** A master password stored in `user.json` (hashed with bcrypt) is required to access stored passwords.
- **Password Management:**
  - View stored passwords in an obscured format.
  - Copy passwords directly without revealing them.
  - Add, edit, and delete passwords securely.
- **Two-Factor Authentication (2FA):** Enhancing security with an additional authentication step.
- **Cross-Platform:** Compatible with Windows, macOS, and Linux.

## Installation
### Prerequisites
Ensure you have the following installed on your system:
- [Node.js](https://nodejs.org/)
- [Git](https://git-scm.com/)

### Steps to Install
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/cardea-passkeeper.git
    ```
2. Navigate to the project directory:
    ```bash
    cd cardea-passkeeper
    ```
3. Install dependencies:
    ```bash
    npm install
    ```
4. Start the application:
    ```bash
    npm run start
    ```

## Usage
1. On the first launch, you will be prompted to set up your master password.
2. Use the dashboard to create, edit, and manage your stored passwords.
3. Click on a password entry to copy it to the clipboard without displaying it.
4. Use the "Generate Password" feature to create strong passwords and store them instantly.

## Security Considerations
- Never share your master password with anyone.
- Store the `user.json` file securely, as it contains the hashed master password.
- Use a strong and unique master password.
- Regularly back up your encrypted `passwords.json` file.

## Project Status
This project is currently under development. New features and improvements are being added regularly.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries or issues, feel free to reach out:
- **Email:** your.email@example.com
- **GitHub:** [yourusername](https://github.com/yourusername)

