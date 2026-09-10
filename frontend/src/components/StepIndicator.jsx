function StepIndicator({ currentStep }) {
    const steps = [
        { number: 1, label: 'Paket & SIM' },
        { number: 2, label: 'Cihaz' },
        { number: 3, label: 'Adres' },
        { number: 4, label: 'Onay' },
    ];

    return (
        <div className="step-indicator">
            {steps.map((step, index) => (
                <div key={step.number} className="step-indicator-item">
                    <div
                        className={`step-circle ${currentStep === step.number ? 'active' : ''} ${
                            currentStep > step.number ? 'completed' : ''
                        }`}
                    >
                        {step.number}
                    </div>
                    <span className="step-label">{step.label}</span>
                    {index < steps.length - 1 && <div className="step-line" />}
                </div>
            ))}
        </div>
    );
}

export default StepIndicator;