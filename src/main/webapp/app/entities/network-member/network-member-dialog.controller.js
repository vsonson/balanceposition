(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NetworkMemberDialogController', NetworkMemberDialogController);

    NetworkMemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NetworkMember', 'UserInfo'];

    function NetworkMemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NetworkMember, UserInfo) {
        var vm = this;

        vm.networkMember = entity;
        vm.clear = clear;
        vm.save = save;
        vm.userinfos = UserInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.networkMember.id !== null) {
                NetworkMember.update(vm.networkMember, onSaveSuccess, onSaveError);
            } else {
                NetworkMember.save(vm.networkMember, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:networkMemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
