(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mood-datum', {
            parent: 'entity',
            url: '/mood-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MoodData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mood-datum/mood-data.html',
                    controller: 'MoodDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('mood-datum-detail', {
            parent: 'mood-datum',
            url: '/mood-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MoodDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mood-datum/mood-datum-detail.html',
                    controller: 'MoodDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MoodDatum', function($stateParams, MoodDatum) {
                    return MoodDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mood-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mood-datum-detail.edit', {
            parent: 'mood-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mood-datum/mood-datum-dialog.html',
                    controller: 'MoodDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MoodDatum', function(MoodDatum) {
                            return MoodDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mood-datum.new', {
            parent: 'mood-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mood-datum/mood-datum-dialog.html',
                    controller: 'MoodDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mood-datum', null, { reload: 'mood-datum' });
                }, function() {
                    $state.go('mood-datum');
                });
            }]
        })
        .state('mood-datum.edit', {
            parent: 'mood-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mood-datum/mood-datum-dialog.html',
                    controller: 'MoodDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MoodDatum', function(MoodDatum) {
                            return MoodDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mood-datum', null, { reload: 'mood-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mood-datum.delete', {
            parent: 'mood-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mood-datum/mood-datum-delete-dialog.html',
                    controller: 'MoodDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MoodDatum', function(MoodDatum) {
                            return MoodDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mood-datum', null, { reload: 'mood-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
