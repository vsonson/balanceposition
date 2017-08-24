(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('KeyPairDeleteController',KeyPairDeleteController);

    KeyPairDeleteController.$inject = ['$uibModalInstance', 'entity', 'KeyPair'];

    function KeyPairDeleteController($uibModalInstance, entity, KeyPair) {
        var vm = this;

        vm.keyPair = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            KeyPair.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
