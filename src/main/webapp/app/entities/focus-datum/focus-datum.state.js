(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('focus-datum', {
            parent: 'entity',
            url: '/focus-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FocusData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/focus-datum/focus-data.html',
                    controller: 'FocusDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('focus-datum-detail', {
            parent: 'focus-datum',
            url: '/focus-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FocusDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/focus-datum/focus-datum-detail.html',
                    controller: 'FocusDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FocusDatum', function($stateParams, FocusDatum) {
                    return FocusDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'focus-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('focus-datum-detail.edit', {
            parent: 'focus-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/focus-datum/focus-datum-dialog.html',
                    controller: 'FocusDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FocusDatum', function(FocusDatum) {
                            return FocusDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('focus-datum.new', {
            parent: 'focus-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/focus-datum/focus-datum-dialog.html',
                    controller: 'FocusDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                level: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('focus-datum', null, { reload: 'focus-datum' });
                }, function() {
                    $state.go('focus-datum');
                });
            }]
        })
        .state('focus-datum.edit', {
            parent: 'focus-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/focus-datum/focus-datum-dialog.html',
                    controller: 'FocusDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FocusDatum', function(FocusDatum) {
                            return FocusDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('focus-datum', null, { reload: 'focus-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('focus-datum.delete', {
            parent: 'focus-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/focus-datum/focus-datum-delete-dialog.html',
                    controller: 'FocusDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FocusDatum', function(FocusDatum) {
                            return FocusDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('focus-datum', null, { reload: 'focus-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
