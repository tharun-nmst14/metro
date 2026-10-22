(function () {
    "use strict";

    document.addEventListener("input", function (event) {
        const field = event.target;
        if (!(field instanceof HTMLInputElement) || !field.classList.contains("sq-order-quantity-input")) {
            return;
        }

        const form = field.form;
        const fields = Array.from(form.querySelectorAll(".sq-order-quantity-input"));
        const dirty = fields.some(function (input) {
            const raw = input.value.trim();
            const number = Number(raw);
            return !/^[0-9]+$/.test(raw) ||
                !Number.isSafeInteger(number) ||
                number > 2147483647 ||
                number !== Number(input.dataset.savedQuantity);
        });

        const save = form.querySelector('[aria-label="Save"]');
        const release = form.querySelector('[aria-label="Release"]');
        save.disabled = !dirty;
        release.disabled = dirty || release.dataset.releaseAvailable !== "true";

        if (dirty) {
            const message = form.closest(".sq-results").querySelector(".sq-action-message");
            if (message) message.textContent = "";
        }
    });
})();
