function toggleSection(sectionName) {
    deactivateAllSections();

    const activeSection = document.querySelector('.' + sectionName);
    if (activeSection) {
        activeSection.classList.add('active');
    }
}

function deactivateAllSections() {
    const sections = document.querySelectorAll('.dashboard, .token, .addProduct, .ViewAllP, .Photo');
    sections.forEach(section => {
        section.classList.remove('active');
    });
}